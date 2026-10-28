package com.mvpmodular.core.network

import com.mvpmodular.core.common.AppLogger
import org.koin.core.module.Module
import org.koin.dsl.module

val networkModule: Module = module {
    single { NetworkConfig() }
    single { createHttpClient(platformHttpEngine(), get<AppLogger>()) }
    single<BffClient> { KtorBffClient(get(), get()) }
}
