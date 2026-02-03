package com.tunehub.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.tunehub.app.ui.components.PlaybackHistoryList

@Composable
fun HomeScreen() {
    Text(text = "Home")
}

@Composable
fun SearchScreen() {
    Text(text = "Search")
}

@Composable
fun PlayerScreen() {
    Column {
        Text(text = "Player")
        PlaybackHistoryList(items = emptyList())
    }
}

@Composable
fun PlaylistScreen() {
    Text(text = "Playlist")
}

@Composable
fun ProfileScreen() {
    Text(text = "Profile")
}

@Composable
fun SettingsScreen() {
    Text(text = "Settings")
}
