package com.tunehub.app.data.model

data class SearchResultDto(
    val keyword: String? = null,
    val total: Int? = null,
    val results: List<SearchSongDto>? = null,
)

data class SearchSongDto(
    val id: String? = null,
    val name: String? = null,
    val artist: String? = null,
    val album: String? = null,
    val url: String? = null,
    val platform: String? = null,
)
