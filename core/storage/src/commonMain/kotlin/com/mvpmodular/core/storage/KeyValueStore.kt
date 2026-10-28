package com.mvpmodular.core.storage

interface KeyValueStore {
    suspend fun get(key: String): String?

    suspend fun put(key: String, value: String)

    suspend fun remove(key: String)

    suspend fun clear()
}
