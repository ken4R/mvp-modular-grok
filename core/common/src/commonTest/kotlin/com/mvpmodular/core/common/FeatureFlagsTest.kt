package com.mvpmodular.core.common

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FeatureFlagsTest {
    @Test
    fun defaultsKeepPaymentsAndPushOff() {
        val flags = StaticFeatureFlags()
        assertFalse(flags.isEnabled(FeatureKey.Payments))
        assertFalse(flags.isEnabled(FeatureKey.PushNotifications))
        assertTrue(flags.isEnabled(FeatureKey.ProfileEdit))
    }

    @Test
    fun explicitSetEnablesAFlagWithoutLoadingCode() {
        val flags = StaticFeatureFlags(enabled = setOf(FeatureKey.Payments))
        assertTrue(flags.isEnabled(FeatureKey.Payments))
        assertFalse(flags.isEnabled(FeatureKey.ProfileEdit))
    }
}
