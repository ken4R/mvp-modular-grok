package com.mvpmodular.core.common

fun interface FeatureFlags {
    fun isEnabled(key: FeatureKey): Boolean
}
