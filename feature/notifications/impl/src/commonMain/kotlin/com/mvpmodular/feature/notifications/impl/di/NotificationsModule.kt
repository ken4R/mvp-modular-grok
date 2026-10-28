package com.mvpmodular.feature.notifications.impl.di

import com.mvpmodular.feature.notifications.api.domain.PushRegistrar
import com.mvpmodular.feature.notifications.api.domain.RegisterPushUseCase
import com.mvpmodular.feature.notifications.impl.data.createPushRegistrar
import com.mvpmodular.feature.notifications.impl.presentation.NotificationsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val notificationsModule: Module = module {
    single<PushRegistrar> { createPushRegistrar() }
    factory { RegisterPushUseCase(get(), get()) }
    viewModelOf(::NotificationsViewModel)
}
