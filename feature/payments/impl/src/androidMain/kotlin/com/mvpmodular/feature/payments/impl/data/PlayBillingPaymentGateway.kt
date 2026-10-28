package com.mvpmodular.feature.payments.impl.data

import com.mvpmodular.core.common.DomainError
import com.mvpmodular.core.common.Outcome
import com.mvpmodular.feature.payments.api.domain.PaymentGateway
import com.mvpmodular.feature.payments.api.domain.PurchaseReceipt

internal actual fun createPaymentGateway(): PaymentGateway = PlayBillingPaymentGateway()

internal class PlayBillingPaymentGateway : PaymentGateway {
    override suspend fun purchase(productId: String): Outcome<PurchaseReceipt, DomainError> =
        Outcome.Failure(DomainError.Unknown("Play Billing n'est pas branché dans ce MVP."))
}
