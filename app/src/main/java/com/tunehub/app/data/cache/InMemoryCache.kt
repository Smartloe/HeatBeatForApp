package com.tunehub.app.data.cache

class InMemoryCache<K, V>(
    private val ttlMillis: Long,
) {
    private data class Entry<V>(val value: V, val timestamp: Long)

    private val storage = mutableMapOf<K, Entry<V>>()

    @Synchronized
    fun get(key: K): V? {
        val entry = storage[key] ?: return null
        val isExpired = System.currentTimeMillis() - entry.timestamp > ttlMillis
        return if (isExpired) {
            storage.remove(key)
            null
        } else {
            entry.value
        }
    }

    @Synchronized
    fun put(key: K, value: V) {
        storage[key] = Entry(value, System.currentTimeMillis())
    }

    suspend fun getOrPut(key: K, loader: suspend () -> V): V {
        return get(key) ?: loader().also { put(key, it) }
    }
}
