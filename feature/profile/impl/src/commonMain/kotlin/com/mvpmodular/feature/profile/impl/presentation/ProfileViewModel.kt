package com.mvpmodular.feature.profile.impl.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvpmodular.core.common.Outcome
import com.mvpmodular.feature.profile.api.domain.GetProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(private val getProfile: GetProfileUseCase) : ViewModel() {
    private val stateFlow = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = stateFlow.asStateFlow()

    fun load(userId: String) {
        viewModelScope.launch {
            stateFlow.update { current -> current.copy(isLoading = true, errorMessage = null) }
            when (val result = getProfile(userId)) {
                is Outcome.Success -> stateFlow.update { current ->
                    current.copy(isLoading = false, profile = result.value)
                }
                is Outcome.Failure -> stateFlow.update { current ->
                    current.copy(isLoading = false, errorMessage = result.error.message)
                }
            }
        }
    }
}
