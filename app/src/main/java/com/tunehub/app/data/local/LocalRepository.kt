package com.tunehub.app.data.local

import com.tunehub.app.domain.model.AudioQuality
import kotlinx.coroutines.flow.Flow

class LocalRepository(
    private val database: AppDatabase,
    private val preferencesStore: UserPreferencesStore,
) {
    private val favoriteDao = database.favoriteDao()
    private val historyDao = database.playbackHistoryDao()

    suspend fun addFavorite(track: FavoriteTrackEntity) {
        favoriteDao.insert(track)
    }

    suspend fun removeFavorite(track: FavoriteTrackEntity) {
        favoriteDao.delete(track)
    }

    suspend fun getFavorites(): List<FavoriteTrackEntity> {
        return favoriteDao.getAll()
    }

    suspend fun addHistory(item: PlaybackHistoryEntity) {
        historyDao.insert(item)
    }

    suspend fun getRecentHistory(limit: Int = 50): List<PlaybackHistoryEntity> {
        return historyDao.getRecent(limit)
    }

    suspend fun clearHistory() {
        historyDao.clear()
    }

    fun observePreferences(): Flow<UserPreferences> = preferencesStore.preferencesFlow

    suspend fun updateQuality(quality: AudioQuality) {
        preferencesStore.updateQuality(quality)
    }

    suspend fun updateWifiOnly(enabled: Boolean) {
        preferencesStore.updateWifiOnly(enabled)
    }
}
