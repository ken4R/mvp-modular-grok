package com.mvpmodular.feature.auth.api.domain

import com.mvpmodular.core.common.DomainError
import com.mvpmodular.core.common.Outcome

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(credentials: Credentials): Outcome<Session, DomainError> {
        val email = credentials.email.trim()
        if (email.isEmpty() || !email.contains("@") || !email.contains(".")) {
            return Outcome.Failure(DomainError.Validation("Email invalide"))
        }
        if (credentials.password.length < MIN_PASSWORD_LENGTH) {
            return Outcome.Failure(DomainError.Validation("Mot de passe trop court"))
        }
        return repository.login(credentials.copy(email = email))
    }

    private companion object {
        const val MIN_PASSWORD_LENGTH = 8
    }
}
