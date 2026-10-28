package com.mvpmodular.core.storage

import org.koin.core.module.Module
import org.koin.dsl.module

val storageModule: Module = module {
    single<KeyValueStore> { InMemoryKeyValueStore() }
}
