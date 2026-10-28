package com.mvpmodular.feature.notifications.api.domain

import com.mvpmodular.core.common.DomainError
import com.mvpmodular.core.common.FeatureFlags
import com.mvpmodular.core.common.FeatureKey
import com.mvpmodular.core.common.Outcome

class RegisterPushUseCase(private val registrar: PushRegistrar, private val featureFlags: FeatureFlags) {
    suspend operator fun invoke(): Outcome<PushToken, DomainError> {
        if (!featureFlags.isEnabled(FeatureKey.PushNotifications)) {
            return Outcome.Failure(DomainError.Unknown("Notifications désactivées"))
        }
        return registrar.register()
    }
}
