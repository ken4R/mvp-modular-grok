# Agents

Instructions for coding agents working in this repository. The product docs are in French (`README.md`, `docs/`). This file is English so implementation agents share one vocabulary.

## Stack

| Piece | Choice |
| --- | --- |
| Shared logic | Kotlin Multiplatform 2.4.21 |
| Android UI | Jetpack Compose, `compileSdk` 37, `minSdk` 26, `targetSdk` 36 |
| iOS UI | SwiftUI, deployment target 16.0, static framework `Shared` |
| DI | Koin 4.2 |
| HTTP | Ktor 3.6, `BffClient` in `:core:network` |
| Async | Coroutines, `StateFlow` ViewModels |
| iOS interop | SKIE 0.10.15, applied only when the host is macOS |
| Errors | `Outcome<T, E>` and sealed `DomainError` |
| Flags | `FeatureFlags` / `StaticFeatureFlags` in `:core:common` |
| Build | Gradle 9.7, AGP 9.3.1, JDK 21, version catalog `gradle/libs.versions.toml`, convention plugins in `build-logic` |

Apple targets (`iosArm64`, `iosSimulatorArm64`) are registered only when `os.name` contains `mac`. Linux builds the Android and shared JVM/Android compilations and the host tests. It does not compile Kotlin/Native or SKIE.

## Module map

```
androidApp          Compose app. Depends only on :shared.
iosApp              SwiftUI app. Links the Shared framework.
shared              Umbrella. Exports one static iOS framework.
core:common         Outcome, DomainError, logger, feature flags.
core:network        Ktor engine and BffClient.
core:storage        KeyValueStore. In-memory. SQLDelight schema is not compiled.
core:di             Aggregates core Koin modules.
feature:<name>:api  Public contract: models, repository interfaces, use cases.
feature:<name>:impl Repository, platform actuals, ViewModel, Koin module.
architecture-test   Konsist checks on feature and core sources.
```

Features today: `auth`, `profile`, `payments`, `notifications`.

Package root: `com.mvpmodular`. A feature api type lives in `com.mvpmodular.feature.<name>.api`. An impl type lives in `com.mvpmodular.feature.<name>.impl` and is `internal`, except the ViewModel, its `UiState`, and the Koin `Module` (those are public because `:shared` and the apps must see them).

## Dependency rules

A feature depends only on other features' `api` modules and on `:core:*`. It never depends on another feature's `impl`.

| Module | May depend on | Must not depend on |
| --- | --- | --- |
| `:feature:*:api` | `:core:common`, other `:feature:*:api` | any impl, network, storage, di, shared, androidApp |
| `:feature:*:impl` | its own api, other apis, `:core:*` | another impl, `:shared`, `:androidApp` |
| `:core:common` | libraries only | any project module |
| `:core:network`, `:core:storage` | `:core:common` | features, di, apps |
| `:core:di` | other `:core:*` | features, apps |
| `:shared` | feature impls and `:core:di` | `:androidApp` |
| `:androidApp` | `:shared` | a feature or core module directly |

Enforced by `checkModuleBoundaries` (Gradle task, also on `check`) and by Konsist in `:architecture-test`. The dependency-analysis Gradle plugin is not used: the api/impl rule is more specific than unused-dependency advice.

Gradle project names for nested modules are all `api` or `impl`. The default capability is `group:name`, so each project's `group` is `com.mvpmodular.` plus its path (`com.mvpmodular.feature.auth.impl`). Do not reset `group` to a shared value. Archive names and Kotlin module names follow the path (`feature-auth-impl`).

Linking is compile-time only. No `ServiceLoader`, no reflection scan for features, no runtime module download. A feature flag set to false fails the use case with `DomainError.Unknown`. The code stays in the binary.

## Conventions

- Domain functions return `Outcome`. Rethrow `CancellationException`. Do not use exceptions for validation, auth, not-found, or network failures.
- Use cases live in the api module. They depend on repository interfaces, not on Ktor or Koin.
- Repositories and platform gateways are `internal` in the impl.
- ViewModels expose `StateFlow`. Android collects with `collectAsStateWithLifecycle`. iOS uses a thin `@MainActor` wrapper and `for await` on the flow (SKIE).
- Koin modules are `val <feature>Module`. Register them in `featureModules()` inside `shared`. Add a getter on `SharedGraph` when iOS needs the ViewModel.
- `:shared` exports api and impl for iOS. Update `mvp.kmp.shared.gradle.kts` when a feature is added.
- Payments and push stay behind `PaymentGateway` and `PushRegistrar`. Platform types use `expect`/`actual` and return `Outcome.Failure` until a real SDK is wired. Check `FeatureKey.Payments` and `FeatureKey.PushNotifications` before doing work.
- Network calls go through `BffClient`. Default base URL: `https://bff.mvpmodular.local`. There is no server in this repo. Login still uses `FakeAuthRepository` (story 1.6).
- Demo account: `demo@mvp.dev` / `password123`.
- UI strings that the user sees are French.
- `compileSdk` stays 37 because OkHttp 5.5 (pulled by Ktor) requires it. The SDK package id is `platforms;android-37.0`.

## Commands

```bash
./gradlew build
./gradlew checkModuleBoundaries
./gradlew :androidApp:assembleDebug
./gradlew :feature:auth:api:testAndroidHostTest
./gradlew :feature:auth:impl:testAndroidHostTest
```

Host tests are the KMP `commonTest` source set on the Android host (`withHostTest`). On a Mac, iOS is the Xcode project `iosApp/MvpModular.xcodeproj`. The Run Script phase calls `:shared:embedAndSignAppleFrameworkForXcode`. SwiftLint: `swiftlint lint --config .swiftlint.yml iosApp`.

## BMAD workflow

The repo installs [BMAD Method](https://github.com/bmad-code-org/BMAD-METHOD) 6.12 (`npx bmad-method install`, module `bmm`, tool `cursor`). Skills land in `.agents/skills/`. Project overrides live in `_bmad/custom/`. Generated planning output belongs in `_bmad-output/` and is gitignored. The durable product docs stay in `docs/`.

`uv` runs the Python helpers those skills call (`uv run …/resolve_customization.py`).

How a change moves:

1. **PRD** — analyst (`bmad-agent-analyst`) and PM (`bmad-agent-pm`) update `docs/prd.md`. Scope stays auth, profile, payments, notifications unless the PRD changes.
2. **Architecture** — architect (`bmad-agent-architect`, Winston). Read `docs/architecture.md` and `_bmad/custom/bmad-agent-architect.toml`. New decisions become ADRs. Do not introduce runtime module loading, a second DI graph, or a feature impl depending on another impl.
3. **Stories** — scrum master / story writer. Shard an epic into `docs/stories/`. Follow the existing story shape: Status, Story, Acceptance Criteria, Tasks, Dev Notes, File List, Change Log. Official skill: `bmad-create-epics-and-stories`. The dev agent must be able to implement from the story without inventing a module layout.
4. **Dev** — `bmad-agent-dev` or `bmad-build`, plus the project skills `new-feature-module` and `add-use-case`. Stay inside the story's file list. Run the module's host tests and `checkModuleBoundaries`.
5. **QA** — `bmad-code-review` and the project skill `review`. Check dependency rules, `Outcome`, flags, and that iOS and Android still share the ViewModel. Say explicitly when iOS was not compiled.

Cursor rules in `.cursor/rules/` scope Kotlin, Compose, SwiftUI, tests, and the six roles. Project skills in `.cursor/skills/` are the repeatable implementation recipes. Prefer those over re-deriving the module layout.
