package com.mvpmodular.feature.profile.api.domain

import com.mvpmodular.core.common.DomainError
import com.mvpmodular.core.common.Outcome

interface ProfileRepository {
    suspend fun getProfile(userId: String): Outcome<UserProfile, DomainError>
}
