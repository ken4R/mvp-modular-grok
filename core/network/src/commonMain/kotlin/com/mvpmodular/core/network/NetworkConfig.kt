package com.mvpmodular.core.network

data class NetworkConfig(val baseUrl: String = "https://bff.mvpmodular.local") {
    fun endpoint(path: String): String {
        val suffix = if (path.startsWith("/")) path else "/$path"
        return baseUrl.trimEnd('/') + suffix
    }
}
