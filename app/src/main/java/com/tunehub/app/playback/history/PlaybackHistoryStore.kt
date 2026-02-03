package com.tunehub.app.playback.history

import java.util.ArrayDeque

class PlaybackHistoryStore(
    private val maxSize: Int = 50,
) {
    private val items = ArrayDeque<PlaybackHistoryItem>()

    @Synchronized
    fun add(item: PlaybackHistoryItem) {
        items.removeAll { it.id == item.id }
        items.addFirst(item)
        while (items.size > maxSize) {
            items.removeLast()
        }
    }

    @Synchronized
    fun getRecent(): List<PlaybackHistoryItem> = items.toList()
}

data class PlaybackHistoryItem(
    val id: String,
    val title: String,
    val artist: String,
    val artworkUrl: String,
)
