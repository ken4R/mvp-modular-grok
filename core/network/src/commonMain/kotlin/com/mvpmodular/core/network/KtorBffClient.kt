package com.mvpmodular.core.network

import com.mvpmodular.core.common.DomainError
import com.mvpmodular.core.common.Outcome
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.CancellationException

class KtorBffClient(private val httpClient: HttpClient, private val config: NetworkConfig) : BffClient {
    override suspend fun get(path: String): Outcome<String, DomainError> =
        guarded { httpClient.get(config.endpoint(path)).bodyAsText() }

    override suspend fun post(path: String, body: String): Outcome<String, DomainError> = guarded {
        httpClient
            .post(config.endpoint(path)) {
                contentType(ContentType.Application.Json)
                setBody(body)
            }.bodyAsText()
    }

    private suspend fun guarded(block: suspend () -> String): Outcome<String, DomainError> = try {
        Outcome.Success(block())
    } catch (cancellation: CancellationException) {
        throw cancellation
    } catch (exception: Exception) {
        Outcome.Failure(DomainError.Network(exception.message ?: "Erreur réseau"))
    }
}
