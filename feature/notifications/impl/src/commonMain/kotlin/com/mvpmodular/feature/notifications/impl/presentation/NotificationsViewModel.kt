package com.mvpmodular.feature.notifications.impl.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvpmodular.core.common.Outcome
import com.mvpmodular.feature.notifications.api.domain.RegisterPushUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationsViewModel(private val registerPush: RegisterPushUseCase) : ViewModel() {
    private val stateFlow = MutableStateFlow(NotificationsUiState())
    val state: StateFlow<NotificationsUiState> = stateFlow.asStateFlow()

    fun register() {
        viewModelScope.launch {
            stateFlow.update { current -> current.copy(isRegistering = true, errorMessage = null) }
            when (val result = registerPush()) {
                is Outcome.Success -> stateFlow.update { current ->
                    current.copy(isRegistering = false, token = result.value)
                }
                is Outcome.Failure -> stateFlow.update { current ->
                    current.copy(isRegistering = false, errorMessage = result.error.message)
                }
            }
        }
    }
}
