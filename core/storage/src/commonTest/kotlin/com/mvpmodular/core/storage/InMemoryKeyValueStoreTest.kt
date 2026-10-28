package com.mvpmodular.core.storage

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class InMemoryKeyValueStoreTest {
    @Test
    fun putThenGetRoundTrips() = runTest {
        val store = InMemoryKeyValueStore()
        store.put("session", "token")
        assertEquals("token", store.get("session"))
        store.remove("session")
        assertNull(store.get("session"))
    }
}
