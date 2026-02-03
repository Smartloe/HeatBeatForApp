package com.tunehub.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.tunehub.app.playback.history.PlaybackHistoryItem

@Composable
fun PlaybackHistoryList(items: List<PlaybackHistoryItem>) {
    if (items.isEmpty()) {
        Text(text = "暂无播放历史")
        return
    }
    Column {
        items.forEach { item ->
            Text(text = "${item.title} - ${item.artist}")
        }
    }
}
