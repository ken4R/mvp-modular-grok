package com.mvpmodular.feature.payments.impl.presentation

import com.mvpmodular.feature.payments.api.domain.PurchaseReceipt

data class PaymentsUiState(
    val isSubmitting: Boolean = false,
    val receipt: PurchaseReceipt? = null,
    val errorMessage: String? = null,
)
