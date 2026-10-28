package com.mvpmodular.feature.payments.api.domain

import com.mvpmodular.core.common.DomainError
import com.mvpmodular.core.common.FeatureKey
import com.mvpmodular.core.common.Outcome
import com.mvpmodular.core.common.StaticFeatureFlags
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class PurchaseProductUseCaseTest {
    @Test
    fun staysOffWhenTheFlagIsDisabled() = runTest {
        val gateway = RecordingGateway()
        val useCase = PurchaseProductUseCase(gateway, StaticFeatureFlags())
        val result = useCase("sku_plus")
        assertIs<DomainError.Unknown>(assertIs<Outcome.Failure<DomainError>>(result).error)
        assertEquals(0, gateway.calls)
    }

    @Test
    fun delegatesWhenTheFlagIsEnabled() = runTest {
        val gateway = RecordingGateway()
        val flags = StaticFeatureFlags(enabled = setOf(FeatureKey.Payments))
        val result = PurchaseProductUseCase(gateway, flags)("sku_plus")
        assertEquals("sku_plus", assertIs<Outcome.Success<PurchaseReceipt>>(result).value.productId)
        assertEquals(1, gateway.calls)
    }
}

private class RecordingGateway : PaymentGateway {
    var calls: Int = 0

    override suspend fun purchase(productId: String): Outcome<PurchaseReceipt, DomainError> {
        calls += 1
        return Outcome.Success(PurchaseReceipt(productId = productId, transactionId = "tx"))
    }
}
