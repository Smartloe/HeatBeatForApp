package com.tunehub.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_tracks")
data class FavoriteTrackEntity(
    @PrimaryKey val id: String,
    val name: String,
    val artist: String,
    val album: String,
    val addedAt: Long,
)
