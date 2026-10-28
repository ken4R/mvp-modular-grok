package com.mvpmodular.feature.profile.impl.data

import com.mvpmodular.core.common.DomainError
import com.mvpmodular.core.common.Outcome
import com.mvpmodular.feature.profile.api.domain.ProfileRepository
import com.mvpmodular.feature.profile.api.domain.UserProfile

internal class FakeProfileRepository : ProfileRepository {
    override suspend fun getProfile(userId: String): Outcome<UserProfile, DomainError> = Outcome.Success(
        UserProfile(
            userId = userId,
            displayName = "Démo",
            email = "demo@mvp.dev",
        ),
    )
}
