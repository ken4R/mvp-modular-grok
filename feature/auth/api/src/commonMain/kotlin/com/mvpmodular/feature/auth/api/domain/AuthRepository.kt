package com.mvpmodular.feature.auth.api.domain

import com.mvpmodular.core.common.DomainError
import com.mvpmodular.core.common.Outcome
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(credentials: Credentials): Outcome<Session, DomainError>

    suspend fun logout(): Outcome<Unit, DomainError>

    fun observeSession(): Flow<Session?>
}
