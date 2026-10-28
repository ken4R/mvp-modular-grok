package com.mvpmodular.feature.auth.impl.di

import com.mvpmodular.feature.auth.api.domain.AuthRepository
import com.mvpmodular.feature.auth.api.domain.LoginUseCase
import com.mvpmodular.feature.auth.impl.data.FakeAuthRepository
import com.mvpmodular.feature.auth.impl.presentation.LoginViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authModule: Module = module {
    single<AuthRepository> { FakeAuthRepository() }
    factory { LoginUseCase(get()) }
    viewModelOf(::LoginViewModel)
}
