package com.mvpmodular.feature.profile.impl.presentation

import com.mvpmodular.feature.profile.api.domain.UserProfile

data class ProfileUiState(
    val isLoading: Boolean = false,
    val profile: UserProfile? = null,
    val errorMessage: String? = null,
)
