package com.tunehub.app.domain.model

data class SearchResult(
    val keyword: String,
    val total: Int,
    val results: List<SearchSong>,
)

data class SearchSong(
    val id: String,
    val name: String,
    val artist: String,
    val album: String,
    val url: String,
    val platform: String,
)
