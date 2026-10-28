package com.mvpmodular.core.network

import com.mvpmodular.core.common.DomainError
import com.mvpmodular.core.common.Outcome

/**
 * Clients talk only to the BFF. Feature data sources must not call downstream services directly.
 */
interface BffClient {
    suspend fun get(path: String): Outcome<String, DomainError>

    suspend fun post(path: String, body: String): Outcome<String, DomainError>
}
