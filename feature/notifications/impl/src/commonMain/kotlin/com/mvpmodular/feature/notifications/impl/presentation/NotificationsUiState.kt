package com.mvpmodular.feature.notifications.impl.presentation

import com.mvpmodular.feature.notifications.api.domain.PushToken

data class NotificationsUiState(
    val isRegistering: Boolean = false,
    val token: PushToken? = null,
    val errorMessage: String? = null,
)
