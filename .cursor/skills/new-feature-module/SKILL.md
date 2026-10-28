---
name: new-feature-module
description: Add a Kotlin Multiplatform feature as api and impl modules, with Koin, a ViewModel, host tests, and thin Android and iOS wrappers. Use when creating a new feature module in this repo.
---

# New feature module

Create `:feature:<name>:api` and `:feature:<name>:impl`. `<name>` is one lowercase word, matching `auth` and `profile`. Do not invent a third Gradle module for data or presentation.

## 1. Gradle

`feature/<name>/api/build.gradle.kts`:

```kotlin
plugins {
    id("mvp.kmp.feature.api")
}
```

`feature/<name>/impl/build.gradle.kts`:

```kotlin
plugins {
    id("mvp.kmp.feature.impl")
}
```

In `settings.gradle.kts`:

```kotlin
include(":feature:<name>:api")
include(":feature:<name>:impl")
```

The feature-impl convention already adds `api(project)` of its own api, Koin, and the lifecycle ViewModel. Add other feature apis or `:core:network` / `:core:storage` only in the impl `build.gradle.kts` when the story needs them. Never add another `:feature:*:impl`.

In `build-logic/convention/src/main/kotlin/mvp.kmp.shared.gradle.kts`, add `api(project(":feature:<name>:impl"))` and, inside the macOS `export` list, both the api and the impl.

## 2. API

Package `com.mvpmodular.feature.<name>.api.domain`.

- Models as data classes.
- A repository interface.
- A use case that returns `Outcome<T, DomainError>`, constructed with the repository. Validate inputs before calling the repository.
- `commonTest` covering success, each validation failure, and a repository failure passed through.

## 3. Impl

Package `com.mvpmodular.feature.<name>.impl`.

- `data`: repository class marked `internal`.
- `presentation`: public `<Name>UiState` and `<Name>ViewModel` (`androidx.lifecycle.ViewModel`) exposing `StateFlow`. Map `Outcome.Failure` to `errorMessage`.
- `di`: public `val <name>Module = module { ... viewModelOf(:: <Name>ViewModel) }`.

If the feature talks to a platform SDK, put the interface in the api and an `internal expect fun` in commonMain, with android and ios actuals that return `Outcome.Failure` until the SDK exists. Gate the use case with `FeatureFlags` when the product flag is off.

`commonTest` for the repository and the ViewModel. ViewModel tests call `Dispatchers.setMain(UnconfinedTestDispatcher())`.

## 4. Composition and UI

- Append `<name>Module` in `featureModules()` in `shared/.../AppBootstrap.kt`.
- Add `fun <name>ViewModel(): <Name>ViewModel = getKoin().get()` on `SharedGraph`.
- Android: a route composable that uses `koinViewModel()` and `collectAsStateWithLifecycle`. The screen composable takes state and lambdas. Strings in French, in `strings.xml`. `androidApp` still depends only on `:shared`.
- iOS: a `@MainActor` wrapper that reads `state.value`, then `for await next in viewModel.state`. A SwiftUI view binds to the wrapper. No business rules in Swift.

## 5. Verify

```bash
./gradlew :feature:<name>:api:testAndroidHostTest :feature:<name>:impl:testAndroidHostTest checkModuleBoundaries
```

On Linux, do not claim the iOS target compiled.
