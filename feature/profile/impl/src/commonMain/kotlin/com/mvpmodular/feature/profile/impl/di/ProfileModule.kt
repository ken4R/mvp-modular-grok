package com.mvpmodular.feature.profile.impl.di

import com.mvpmodular.feature.profile.api.domain.GetProfileUseCase
import com.mvpmodular.feature.profile.api.domain.ProfileRepository
import com.mvpmodular.feature.profile.impl.data.FakeProfileRepository
import com.mvpmodular.feature.profile.impl.presentation.ProfileViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val profileModule: Module = module {
    single<ProfileRepository> { FakeProfileRepository() }
    factory { GetProfileUseCase(get()) }
    viewModelOf(::ProfileViewModel)
}
