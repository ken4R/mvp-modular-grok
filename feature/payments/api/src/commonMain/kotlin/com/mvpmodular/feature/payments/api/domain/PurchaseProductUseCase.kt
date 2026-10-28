package com.mvpmodular.feature.payments.api.domain

import com.mvpmodular.core.common.DomainError
import com.mvpmodular.core.common.FeatureFlags
import com.mvpmodular.core.common.FeatureKey
import com.mvpmodular.core.common.Outcome

class PurchaseProductUseCase(private val gateway: PaymentGateway, private val featureFlags: FeatureFlags) {
    suspend operator fun invoke(productId: String): Outcome<PurchaseReceipt, DomainError> {
        if (!featureFlags.isEnabled(FeatureKey.Payments)) {
            return Outcome.Failure(DomainError.Unknown("Paiements désactivés"))
        }
        if (productId.isBlank()) {
            return Outcome.Failure(DomainError.Validation("Produit manquant"))
        }
        return gateway.purchase(productId.trim())
    }
}
