package com.mvpmodular.feature.profile.api.domain

import com.mvpmodular.core.common.DomainError
import com.mvpmodular.core.common.Outcome
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class GetProfileUseCaseTest {
    @Test
    fun rejectsBlankUserId() = runTest {
        val useCase = GetProfileUseCase(FixedProfileRepository(Outcome.Success(sample())))
        val result = useCase("  ")
        assertIs<DomainError.Validation>(assertIs<Outcome.Failure<DomainError>>(result).error)
    }

    @Test
    fun returnsTheProfile() = runTest {
        val useCase = GetProfileUseCase(FixedProfileRepository(Outcome.Success(sample())))
        val result = useCase("user-1")
        assertEquals("Ada", assertIs<Outcome.Success<UserProfile>>(result).value.displayName)
    }

    private fun sample() = UserProfile(userId = "user-1", displayName = "Ada", email = "ada@mvp.dev")
}

private class FixedProfileRepository(private val result: Outcome<UserProfile, DomainError>) : ProfileRepository {
    override suspend fun getProfile(userId: String): Outcome<UserProfile, DomainError> = result
}
