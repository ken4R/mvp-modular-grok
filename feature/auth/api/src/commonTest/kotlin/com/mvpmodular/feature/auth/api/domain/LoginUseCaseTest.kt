package com.mvpmodular.feature.auth.api.domain

import com.mvpmodular.core.common.DomainError
import com.mvpmodular.core.common.Outcome
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class LoginUseCaseTest {
    @Test
    fun rejectsInvalidEmailWithoutCallingTheRepository() = runTest {
        val repository = RecordingAuthRepository(Outcome.Success(sampleSession()))
        val useCase = LoginUseCase(repository)

        val result = useCase(Credentials("not-an-email", "password123"))

        assertIs<DomainError.Validation>(assertIs<Outcome.Failure<DomainError>>(result).error)
        assertEquals(0, repository.loginCount)
    }

    @Test
    fun rejectsShortPassword() = runTest {
        val useCase = LoginUseCase(RecordingAuthRepository(Outcome.Success(sampleSession())))
        val result = useCase(Credentials("demo@mvp.dev", "short"))
        assertIs<DomainError.Validation>(assertIs<Outcome.Failure<DomainError>>(result).error)
    }

    @Test
    fun trimsEmailAndDelegatesWhenValid() = runTest {
        val repository = RecordingAuthRepository(Outcome.Success(sampleSession()))
        val useCase = LoginUseCase(repository)

        val result = useCase(Credentials("  demo@mvp.dev  ", "password123"))

        assertIs<Outcome.Success<Session>>(result)
        assertEquals("demo@mvp.dev", repository.lastEmail)
        assertEquals(1, repository.loginCount)
    }

    @Test
    fun returnsUnauthorizedFromTheRepository() = runTest {
        val repository = RecordingAuthRepository(
            Outcome.Failure(DomainError.Unauthorized("Identifiants incorrects")),
        )
        val result = LoginUseCase(repository)(Credentials("demo@mvp.dev", "password123"))
        assertIs<DomainError.Unauthorized>(assertIs<Outcome.Failure<DomainError>>(result).error)
        assertTrue(repository.loginCount == 1)
    }

    private fun sampleSession() = Session(userId = "user-1", email = "demo@mvp.dev", token = "token")
}

private class RecordingAuthRepository(private val result: Outcome<Session, DomainError>) : AuthRepository {
    var loginCount: Int = 0
    var lastEmail: String = ""

    override suspend fun login(credentials: Credentials): Outcome<Session, DomainError> {
        loginCount += 1
        lastEmail = credentials.email
        return result
    }

    override suspend fun logout(): Outcome<Unit, DomainError> = Outcome.Success(Unit)

    override fun observeSession(): Flow<Session?> = flowOf(null)
}
