package com.mvpmodular.feature.auth.impl.presentation

import com.mvpmodular.feature.auth.api.domain.LoginUseCase
import com.mvpmodular.feature.auth.impl.data.DemoAccount
import com.mvpmodular.feature.auth.impl.data.FakeAuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LoginViewModelTest {
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun submitPublishesTheDemoSession() = runTest {
        val viewModel = LoginViewModel(LoginUseCase(FakeAuthRepository()))
        viewModel.onEmailChanged(DemoAccount.EMAIL)
        viewModel.onPasswordChanged(DemoAccount.PASSWORD)

        viewModel.submit()

        val state = viewModel.state.value
        assertEquals(DemoAccount.EMAIL, state.session?.email)
        assertTrue(state.isLoggedIn)
        assertFalse(state.isSubmitting)
    }

    @Test
    fun submitSurfacesUnauthorized() = runTest {
        val viewModel = LoginViewModel(LoginUseCase(FakeAuthRepository()))
        viewModel.onEmailChanged(DemoAccount.EMAIL)
        viewModel.onPasswordChanged("wrong-password")

        viewModel.submit()

        assertEquals("Identifiants incorrects", viewModel.state.value.errorMessage)
        assertFalse(viewModel.state.value.isLoggedIn)
    }
}
