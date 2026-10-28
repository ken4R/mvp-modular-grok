package com.mvpmodular.shared

import com.mvpmodular.core.di.coreModules
import com.mvpmodular.feature.auth.impl.di.authModule
import com.mvpmodular.feature.notifications.impl.di.notificationsModule
import com.mvpmodular.feature.payments.impl.di.paymentsModule
import com.mvpmodular.feature.profile.impl.di.profileModule
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

fun featureModules(): List<Module> = listOf(
    authModule,
    profileModule,
    paymentsModule,
    notificationsModule,
)

fun initAppKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(coreModules() + featureModules())
    }
}

fun startApp() {
    initAppKoin()
}
