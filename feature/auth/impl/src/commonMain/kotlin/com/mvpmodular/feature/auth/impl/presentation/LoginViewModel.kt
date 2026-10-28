package com.mvpmodular.feature.auth.impl.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvpmodular.core.common.Outcome
import com.mvpmodular.feature.auth.api.domain.Credentials
import com.mvpmodular.feature.auth.api.domain.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(private val login: LoginUseCase) : ViewModel() {
    private val stateFlow = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = stateFlow.asStateFlow()

    fun onEmailChanged(email: String) {
        stateFlow.update { current -> current.copy(email = email, errorMessage = null) }
    }

    fun onPasswordChanged(password: String) {
        stateFlow.update { current -> current.copy(password = password, errorMessage = null) }
    }

    fun submit() {
        val snapshot = stateFlow.value
        if (snapshot.isSubmitting || snapshot.isLoggedIn) {
            return
        }
        viewModelScope.launch {
            stateFlow.update { current -> current.copy(isSubmitting = true, errorMessage = null) }
            when (val result = login(Credentials(snapshot.email, snapshot.password))) {
                is Outcome.Success -> stateFlow.update { current ->
                    current.copy(isSubmitting = false, session = result.value)
                }
                is Outcome.Failure -> stateFlow.update { current ->
                    current.copy(isSubmitting = false, errorMessage = result.error.message)
                }
            }
        }
    }
}
