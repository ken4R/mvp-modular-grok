package com.mvpmodular.core.network

import com.mvpmodular.core.common.AppLogger
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging

expect fun platformHttpEngine(): HttpClientEngine

fun createHttpClient(engine: HttpClientEngine, logger: AppLogger): HttpClient = HttpClient(engine) {
    expectSuccess = false
    install(Logging) {
        this.logger = object : Logger {
            override fun log(message: String) {
                logger.debug("Ktor", message)
            }
        }
        level = LogLevel.INFO
    }
}
