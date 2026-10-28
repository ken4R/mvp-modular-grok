package com.mvpmodular.shared

import com.mvpmodular.feature.auth.impl.presentation.LoginViewModel
import com.mvpmodular.feature.notifications.impl.presentation.NotificationsViewModel
import com.mvpmodular.feature.payments.impl.presentation.PaymentsViewModel
import com.mvpmodular.feature.profile.impl.presentation.ProfileViewModel
import org.koin.mp.KoinPlatform

object SharedGraph {
    fun loginViewModel(): LoginViewModel = KoinPlatform.getKoin().get()

    fun profileViewModel(): ProfileViewModel = KoinPlatform.getKoin().get()

    fun paymentsViewModel(): PaymentsViewModel = KoinPlatform.getKoin().get()

    fun notificationsViewModel(): NotificationsViewModel = KoinPlatform.getKoin().get()
}
