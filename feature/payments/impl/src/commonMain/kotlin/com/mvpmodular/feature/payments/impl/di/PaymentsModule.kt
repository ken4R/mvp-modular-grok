package com.mvpmodular.feature.payments.impl.di

import com.mvpmodular.feature.payments.api.domain.PaymentGateway
import com.mvpmodular.feature.payments.api.domain.PurchaseProductUseCase
import com.mvpmodular.feature.payments.impl.data.createPaymentGateway
import com.mvpmodular.feature.payments.impl.presentation.PaymentsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val paymentsModule: Module = module {
    single<PaymentGateway> { createPaymentGateway() }
    factory { PurchaseProductUseCase(get(), get()) }
    viewModelOf(::PaymentsViewModel)
}
