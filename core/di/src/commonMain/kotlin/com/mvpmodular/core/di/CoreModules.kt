package com.mvpmodular.core.di

import com.mvpmodular.core.common.AppLogger
import com.mvpmodular.core.common.ConsoleAppLogger
import com.mvpmodular.core.common.FeatureFlags
import com.mvpmodular.core.common.StaticFeatureFlags
import com.mvpmodular.core.network.networkModule
import com.mvpmodular.core.storage.storageModule
import org.koin.core.module.Module
import org.koin.dsl.module

val coreModule: Module = module {
    single<AppLogger> { ConsoleAppLogger() }
    single<FeatureFlags> { StaticFeatureFlags() }
}

fun coreModules(): List<Module> = listOf(coreModule, networkModule, storageModule)
