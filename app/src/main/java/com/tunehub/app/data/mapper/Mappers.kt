package com.tunehub.app.data.mapper

import com.tunehub.app.data.model.SearchResultDto
import com.tunehub.app.data.model.SearchSongDto
import com.tunehub.app.data.model.SongInfoDto
import com.tunehub.app.domain.model.SearchResult
import com.tunehub.app.domain.model.SearchSong
import com.tunehub.app.domain.model.SongInfo

fun SongInfoDto.toDomain(): SongInfo = SongInfo(
    name = name.orEmpty(),
    artist = artist.orEmpty(),
    album = album.orEmpty(),
    url = url.orEmpty(),
    pic = pic.orEmpty(),
    lrc = lrc.orEmpty(),
)

fun SearchResultDto.toDomain(): SearchResult = SearchResult(
    keyword = keyword.orEmpty(),
    total = total ?: (results?.size ?: 0),
    results = results?.map { it.toDomain() }.orEmpty(),
)

fun SearchSongDto.toDomain(): SearchSong = SearchSong(
    id = id.orEmpty(),
    name = name.orEmpty(),
    artist = artist.orEmpty(),
    album = album.orEmpty(),
    url = url.orEmpty(),
    platform = platform.orEmpty(),
)
