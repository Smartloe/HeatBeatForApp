package com.tunehub.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tunehub.app.lyrics.LrcParser
import com.tunehub.app.ui.components.LyricsView
import com.tunehub.app.ui.components.PlaybackHistoryList
import kotlinx.coroutines.delay

@Composable
fun HomeScreen() {
    var showBanner by remember { mutableStateOf(false) }
    val bannerAlpha by animateFloatAsState(if (showBanner) 1f else 0f, label = "bannerAlpha")

    LaunchedEffect(Unit) {
        delay(150)
        showBanner = true
    }

    val featured = listOf("晨间轻音", "海风与日落", "城市夜跑", "通勤舒缓")
    val playlists = listOf("今日精选", "治愈电台", "热门榜单", "独立音乐")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
    ) {
        Text(
            text = "Hello, TuneHub",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "为你推荐轻盈而清新的音乐旅程",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(bannerAlpha),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "清晨模式", fontWeight = FontWeight.SemiBold)
                Text(
                    text = "自动切换到舒缓节奏，开启柔和一天",
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "精选主题", fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(featured) { item ->
                Card(
                    modifier = Modifier.size(width = 140.dp, height = 90.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text(text = item, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "发现歌单", fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(playlists) { item ->
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = item, fontWeight = FontWeight.Medium)
                            Text(text = "今日更新", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchScreen() {
    var query by remember { mutableStateOf("") }
    val suggestions = listOf("周杰伦", "Ambient", "Lo-fi", "晚安曲")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
    ) {
        Text(text = "搜索", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text(text = "搜索歌曲 / 歌单 / 歌手") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(visible = query.isNotBlank()) {
            Text(
                text = "正在为你搜索：$query",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "热门搜索", fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(suggestions) { item ->
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                    Text(text = item, modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp))
                }
            }
        }
    }
}

@Composable
fun PlayerScreen() {
    var progress by remember { mutableStateOf(0.35f) }
    val artScale by animateFloatAsState(targetValue = 1f, label = "artScale")
    val demoLyrics = remember {
        LrcParser.parse(
            """
            [00:00.00]夜航
            [00:05.50]城市的风轻轻经过
            [00:10.20]灯火像星河在流动
            [00:15.60]听见自己心跳
            [00:20.90]慢慢沉入夜色
            """.trimIndent(),
        )
    }
    val activeIndex = remember { LrcParser.findActiveIndex(demoLyrics, 10000L) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "正在播放", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .alpha(artScale),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "夜航", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Text(text = "City Drift", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Slider(value = progress, onValueChange = { progress = it })
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "播放历史", fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        PlaybackHistoryList(items = emptyList())
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "歌词", fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        LyricsView(lines = demoLyrics, activeIndex = activeIndex, onSeek = { })
    }
}

@Composable
fun PlaylistScreen() {
    val lists = listOf("微光精选", "轻快节奏", "温柔人声", "独立浪潮", "城市漫游")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
    ) {
        Text(text = "歌单", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(lists) { item ->
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.secondary),
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = item, fontWeight = FontWeight.Medium)
                            Text(text = "36 首", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
    ) {
        Text(text = "我的", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "TuneHub 旅人", fontWeight = FontWeight.SemiBold)
                Text(text = "今日已听 42 分钟", style = MaterialTheme.typography.bodySmall)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "快捷入口", fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            QuickItem(title = "我喜欢的", subtitle = "收藏 128 首")
            QuickItem(title = "最近播放", subtitle = "同步至所有设备")
            QuickItem(title = "下载管理", subtitle = "离线 24 首")
        }
    }
}

@Composable
fun SettingsScreen() {
    var wifiOnly by remember { mutableStateOf(true) }
    var autoPlay by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
    ) {
        Text(text = "设置", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        SettingToggle(
            title = "仅 Wi-Fi 下载",
            subtitle = "节省流量",
            checked = wifiOnly,
            onCheckedChange = { wifiOnly = it },
        )
        Spacer(modifier = Modifier.height(12.dp))
        SettingToggle(
            title = "自动连续播放",
            subtitle = "根据历史推荐",
            checked = autoPlay,
            onCheckedChange = { autoPlay = it },
        )
    }
}

@Composable
private fun QuickItem(title: String, subtitle: String) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = title, fontWeight = FontWeight.Medium)
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun SettingToggle(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Medium)
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall)
            }
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
    }
}
