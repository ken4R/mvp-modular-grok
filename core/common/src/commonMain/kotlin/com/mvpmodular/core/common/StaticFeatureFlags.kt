package com.mvpmodular.core.common

class StaticFeatureFlags(enabled: Set<FeatureKey> = setOf(FeatureKey.ProfileEdit)) : FeatureFlags {
    private val enabledKeys = enabled.toSet()

    override fun isEnabled(key: FeatureKey): Boolean = key in enabledKeys
}
