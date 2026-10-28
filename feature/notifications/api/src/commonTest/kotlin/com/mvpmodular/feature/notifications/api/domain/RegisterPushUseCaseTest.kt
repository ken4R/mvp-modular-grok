package com.mvpmodular.feature.notifications.api.domain

import com.mvpmodular.core.common.DomainError
import com.mvpmodular.core.common.FeatureKey
import com.mvpmodular.core.common.Outcome
import com.mvpmodular.core.common.StaticFeatureFlags
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class RegisterPushUseCaseTest {
    @Test
    fun staysOffWhenTheFlagIsDisabled() = runTest {
        val registrar = RecordingRegistrar()
        val result = RegisterPushUseCase(registrar, StaticFeatureFlags())()
        assertIs<DomainError.Unknown>(assertIs<Outcome.Failure<DomainError>>(result).error)
        assertEquals(0, registrar.calls)
    }

    @Test
    fun registersWhenTheFlagIsEnabled() = runTest {
        val flags = StaticFeatureFlags(enabled = setOf(FeatureKey.PushNotifications))
        val result = RegisterPushUseCase(RecordingRegistrar(), flags)()
        assertEquals("token", assertIs<Outcome.Success<PushToken>>(result).value.value)
    }
}

private class RecordingRegistrar : PushRegistrar {
    var calls: Int = 0

    override suspend fun register(): Outcome<PushToken, DomainError> {
        calls += 1
        return Outcome.Success(PushToken(value = "token", platform = "test"))
    }
}
