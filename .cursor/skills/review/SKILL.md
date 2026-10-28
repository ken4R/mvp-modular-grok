---
name: review
description: Review a change or PR for this repo's architecture rules. Use when asked to review a PR, check module boundaries, or QA a story.
---

# Architecture review

Review the diff against `AGENTS.md` and `docs/architecture.md`. Report findings first, with file paths. Do not expand the change while reviewing.

## Dependencies

- `:feature:*:impl` does not depend on another `:feature:*:impl`, on `:shared`, or on `:androidApp`.
- `:feature:*:api` depends only on `:core:common` and other feature apis.
- `:core:common` has no project dependencies. `:core:network` and `:core:storage` depend only on `:core:common`. `:core:di` stays inside `:core`.
- `:androidApp` depends only on `:shared`.
- Kotlin imports of `com.mvpmodular.feature.<name>.impl` appear only in that impl, in `:shared`, or in `:androidApp`.
- `group` is still `com.mvpmodular.` plus the project path. A shared group of `com.mvpmodular` is a defect: Gradle will collapse every `impl` onto one project.

`./gradlew checkModuleBoundaries` is the executable form of these rules. If it was not run, say so.

## Domain

- New business operations return `Outcome<T, DomainError>` from a use case in the api module.
- `CancellationException` is rethrown. Other transport failures become `DomainError.Network`.
- Repository and gateway implementations are `internal`.
- ViewModels and Koin modules stay public in the impl.
- New Koin modules are listed in `featureModules()`. iOS-facing ViewModels are on `SharedGraph`.
- No `ServiceLoader`, no reflection over the feature list, no runtime download of a module.
- `FeatureFlags` only change the `Outcome` of a linked use case.
- Network code uses `BffClient` rather than a new HTTP stack.
- Payments and push call `PaymentGateway` / `PushRegistrar`, not StoreKit or Play Billing types from a use case.

## Tests and platforms

- A new or changed use case has a `commonTest`.
- ViewModel tests set `Dispatchers.Main`.
- Name the Gradle tasks that ran and their result.
- iOS and SKIE are unverified on Linux. Do not treat `./gradlew build` as an iOS compile.
- User-visible copy added for the apps is French.

## Story

If a story file exists, walk each acceptance criterion. Update nothing unless the user asked for fixes. A story marked Done needs the code, the tests, and a changelog line.
