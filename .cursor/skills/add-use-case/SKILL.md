---
name: add-use-case
description: Add a use case to an existing feature api module, with Outcome, a repository method if needed, a host test, and a ViewModel hook. Use when extending auth, profile, payments, or notifications.
---

# Add a use case

The use case belongs in `:feature:<name>:api`, next to the existing ones (`feature/<name>/api/src/commonMain/kotlin/.../domain/`).

## Shape

```kotlin
class DoSomethingUseCase(
    private val repository: SomeRepository,
) {
    suspend operator fun invoke(input: Input): Outcome<Output, DomainError> {
        // validate first, return Outcome.Failure(DomainError.Validation(...))
        // then call the repository
    }
}
```

- Return `Outcome`. Do not throw for validation, unauthorized, not found, or network.
- Depend on the repository interface in the same api module. If the data comes from another feature, depend on that feature's api (add the `api(project)` line on this feature's api or impl, whichever needs the type). Do not depend on the other impl.
- If a product flag must be off, inject `FeatureFlags` and fail with `DomainError.Unknown` before the repository call. Do not skip compiling the use case.
- Extend the repository interface only when the use case needs a new operation. Implement it in the existing `internal` repository in the impl module.
- Register the use case in the feature's Koin module if a ViewModel needs it (`factoryOf(::DoSomethingUseCase)`).

## Test

Add `DoSomethingUseCaseTest` in the api `commonTest`.

- Invalid input does not call the repository.
- Success returns `Outcome.Success`.
- A repository `Outcome.Failure` is returned unchanged.

Use a recording fake defined in the test file.

## Presentation

If the story includes UI, add a method on the existing ViewModel that launches the use case and writes `UiState`. Keep the Android composable and the iOS wrapper as collectors. Do not put the use case in the app modules.

## Verify

```bash
./gradlew :feature:<name>:api:testAndroidHostTest checkModuleBoundaries
```

Add the impl host test task when the repository or ViewModel changed.
