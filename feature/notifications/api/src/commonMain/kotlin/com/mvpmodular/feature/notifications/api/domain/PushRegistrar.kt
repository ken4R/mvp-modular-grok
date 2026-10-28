package com.mvpmodular.feature.notifications.api.domain

import com.mvpmodular.core.common.DomainError
import com.mvpmodular.core.common.Outcome

interface PushRegistrar {
    suspend fun register(): Outcome<PushToken, DomainError>
}
