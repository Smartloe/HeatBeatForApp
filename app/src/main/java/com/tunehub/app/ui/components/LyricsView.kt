package com.tunehub.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tunehub.app.domain.model.LyricLine

@Composable
fun LyricsView(
    lines: List<LyricLine>,
    activeIndex: Int,
    onSeek: (Long) -> Unit,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(activeIndex) {
        if (activeIndex >= 0) {
            listState.animateScrollToItem(activeIndex)
        }
    }

    LazyColumn(state = listState) {
        itemsIndexed(lines) { index, item ->
            val isActive = index == activeIndex
            Text(
                text = item.text,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSeek(item.timeMs) }
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
            )
        }
    }
}
