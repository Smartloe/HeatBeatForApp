package com.tunehub.app.auth

import android.content.Context
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.tokenDataStore by preferencesDataStore(name = "auth")

class TokenStore(private val context: Context) {
    private val tokenKey = stringPreferencesKey("token")
    private val expiryKey = longPreferencesKey("expires_at")

    val tokenFlow: Flow<AuthToken?> = context.tokenDataStore.data.map { prefs ->
        val token = prefs[tokenKey]
        val expiresAt = prefs[expiryKey] ?: 0L
        if (token.isNullOrBlank()) null else AuthToken(token, expiresAt)
    }

    suspend fun saveToken(token: String, expiresInSeconds: Long) {
        val expiresAt = System.currentTimeMillis() + expiresInSeconds * 1000
        context.tokenDataStore.edit { prefs ->
            prefs[tokenKey] = token
            prefs[expiryKey] = expiresAt
        }
    }

    suspend fun clear() {
        context.tokenDataStore.edit { prefs ->
            prefs.remove(tokenKey)
            prefs.remove(expiryKey)
        }
    }
}

data class AuthToken(
    val token: String,
    val expiresAt: Long,
) {
    fun isExpired(nowMillis: Long = System.currentTimeMillis()): Boolean = nowMillis >= expiresAt
}
