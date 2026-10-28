package com.mvpmodular.feature.notifications.impl.data

import com.mvpmodular.core.common.DomainError
import com.mvpmodular.core.common.Outcome
import com.mvpmodular.feature.notifications.api.domain.PushRegistrar
import com.mvpmodular.feature.notifications.api.domain.PushToken

internal actual fun createPushRegistrar(): PushRegistrar = ApnsPushRegistrar()

internal class ApnsPushRegistrar : PushRegistrar {
    override suspend fun register(): Outcome<PushToken, DomainError> =
        Outcome.Failure(DomainError.Unknown("APNs n'est pas branché dans ce MVP."))
}
