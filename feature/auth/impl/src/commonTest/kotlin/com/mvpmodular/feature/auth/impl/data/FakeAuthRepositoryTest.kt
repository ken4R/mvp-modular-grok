package com.mvpmodular.feature.auth.impl.data

import com.mvpmodular.core.common.DomainError
import com.mvpmodular.core.common.Outcome
import com.mvpmodular.feature.auth.api.domain.Credentials
import com.mvpmodular.feature.auth.api.domain.Session
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class FakeAuthRepositoryTest {
    @Test
    fun acceptsTheDemoAccount() = runTest {
        val repository = FakeAuthRepository()
        val result = repository.login(Credentials(DemoAccount.EMAIL, DemoAccount.PASSWORD))
        val session = assertIs<Outcome.Success<Session>>(result).value
        assertEquals(DemoAccount.EMAIL, session.email)
        val observed = assertIs<StateFlow<Session?>>(repository.observeSession())
        assertEquals(session, observed.value)
    }

    @Test
    fun rejectsAnythingElse() = runTest {
        val repository = FakeAuthRepository()
        val result = repository.login(Credentials(DemoAccount.EMAIL, "wrong-password"))
        assertIs<DomainError.Unauthorized>(assertIs<Outcome.Failure<DomainError>>(result).error)
        val observed = assertIs<StateFlow<Session?>>(repository.observeSession())
        assertEquals(null, observed.value)
    }
}
