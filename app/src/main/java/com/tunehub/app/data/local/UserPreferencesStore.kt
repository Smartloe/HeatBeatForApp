package com.tunehub.app.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tunehub.app.domain.model.AudioQuality
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.userPreferencesDataStore by preferencesDataStore(name = "user_preferences")

class UserPreferencesStore(private val context: Context) {
    private val qualityKey = stringPreferencesKey("audio_quality")
    private val wifiOnlyKey = booleanPreferencesKey("wifi_only")

    val preferencesFlow: Flow<UserPreferences> = context.userPreferencesDataStore.data.map { prefs ->
        val quality = prefs[qualityKey]?.let { runCatching { AudioQuality.valueOf(it) }.getOrNull() }
            ?: AudioQuality.HIGH
        val wifiOnly = prefs[wifiOnlyKey] ?: true
        UserPreferences(quality = quality, wifiOnly = wifiOnly)
    }

    suspend fun updateQuality(quality: AudioQuality) {
        context.userPreferencesDataStore.edit { prefs ->
            prefs[qualityKey] = quality.name
        }
    }

    suspend fun updateWifiOnly(enabled: Boolean) {
        context.userPreferencesDataStore.edit { prefs ->
            prefs[wifiOnlyKey] = enabled
        }
    }
}

data class UserPreferences(
    val quality: AudioQuality,
    val wifiOnly: Boolean,
)
