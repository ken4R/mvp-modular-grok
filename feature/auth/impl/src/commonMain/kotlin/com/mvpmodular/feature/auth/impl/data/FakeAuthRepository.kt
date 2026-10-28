package com.mvpmodular.feature.auth.impl.data

import com.mvpmodular.core.common.DomainError
import com.mvpmodular.core.common.Outcome
import com.mvpmodular.feature.auth.api.domain.AuthRepository
import com.mvpmodular.feature.auth.api.domain.Credentials
import com.mvpmodular.feature.auth.api.domain.Session
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class FakeAuthRepository : AuthRepository {
    private val session = MutableStateFlow<Session?>(null)

    override suspend fun login(credentials: Credentials): Outcome<Session, DomainError> {
        if (credentials.email == DemoAccount.EMAIL && credentials.password == DemoAccount.PASSWORD) {
            val created = Session(
                userId = DemoAccount.USER_ID,
                email = credentials.email,
                token = DemoAccount.TOKEN,
            )
            session.value = created
            return Outcome.Success(created)
        }
        return Outcome.Failure(DomainError.Unauthorized("Identifiants incorrects"))
    }

    override suspend fun logout(): Outcome<Unit, DomainError> {
        session.value = null
        return Outcome.Success(Unit)
    }

    override fun observeSession(): Flow<Session?> = session.asStateFlow()
}
