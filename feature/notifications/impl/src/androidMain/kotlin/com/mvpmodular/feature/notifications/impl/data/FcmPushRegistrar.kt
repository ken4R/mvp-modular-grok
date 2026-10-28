package com.mvpmodular.feature.notifications.impl.data

import com.mvpmodular.core.common.DomainError
import com.mvpmodular.core.common.Outcome
import com.mvpmodular.feature.notifications.api.domain.PushRegistrar
import com.mvpmodular.feature.notifications.api.domain.PushToken

internal actual fun createPushRegistrar(): PushRegistrar = FcmPushRegistrar()

internal class FcmPushRegistrar : PushRegistrar {
    override suspend fun register(): Outcome<PushToken, DomainError> =
        Outcome.Failure(DomainError.Unknown("FCM n'est pas branché dans ce MVP."))
}
