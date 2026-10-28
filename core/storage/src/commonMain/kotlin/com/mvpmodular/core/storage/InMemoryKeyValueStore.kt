package com.mvpmodular.core.storage

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * MVP stand-in for the SQLDelight cache. The schema sketch lives in schema/AppDatabase.sq.
 */
class InMemoryKeyValueStore : KeyValueStore {
    private val mutex = Mutex()
    private val values = mutableMapOf<String, String>()

    override suspend fun get(key: String): String? = mutex.withLock { values[key] }

    override suspend fun put(key: String, value: String) {
        mutex.withLock { values[key] = value }
    }

    override suspend fun remove(key: String) {
        mutex.withLock { values.remove(key) }
    }

    override suspend fun clear() {
        mutex.withLock { values.clear() }
    }
}
