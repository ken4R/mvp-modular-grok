package com.mvpmodular.feature.profile.api.domain

import com.mvpmodular.core.common.DomainError
import com.mvpmodular.core.common.Outcome

class GetProfileUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(userId: String): Outcome<UserProfile, DomainError> {
        if (userId.isBlank()) {
            return Outcome.Failure(DomainError.Validation("Utilisateur manquant"))
        }
        return repository.getProfile(userId.trim())
    }
}
