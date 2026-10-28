package com.mvpmodular.feature.payments.impl.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvpmodular.core.common.Outcome
import com.mvpmodular.feature.payments.api.domain.PurchaseProductUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PaymentsViewModel(private val purchaseProduct: PurchaseProductUseCase) : ViewModel() {
    private val stateFlow = MutableStateFlow(PaymentsUiState())
    val state: StateFlow<PaymentsUiState> = stateFlow.asStateFlow()

    fun purchase(productId: String) {
        viewModelScope.launch {
            stateFlow.update { current -> current.copy(isSubmitting = true, errorMessage = null) }
            when (val result = purchaseProduct(productId)) {
                is Outcome.Success -> stateFlow.update { current ->
                    current.copy(isSubmitting = false, receipt = result.value)
                }
                is Outcome.Failure -> stateFlow.update { current ->
                    current.copy(isSubmitting = false, errorMessage = result.error.message)
                }
            }
        }
    }
}
