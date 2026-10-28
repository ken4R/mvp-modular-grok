package com.mvpmodular.feature.auth.impl.presentation

import com.mvpmodular.feature.auth.api.domain.Session

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val session: Session? = null,
) {
    val isLoggedIn: Boolean get() = session != null
}
