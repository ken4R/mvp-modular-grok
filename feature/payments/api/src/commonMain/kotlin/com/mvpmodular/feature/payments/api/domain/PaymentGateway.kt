package com.mvpmodular.feature.payments.api.domain

import com.mvpmodular.core.common.DomainError
import com.mvpmodular.core.common.Outcome

interface PaymentGateway {
    suspend fun purchase(productId: String): Outcome<PurchaseReceipt, DomainError>
}
