package com.tunehub.app.domain.repository

import com.tunehub.app.domain.model.ApiResult
import com.tunehub.app.domain.model.SearchResult
import com.tunehub.app.domain.model.SongInfo

interface TuneHubRepository {
    suspend fun getSongInfo(source: String, id: String): ApiResult<SongInfo>

    suspend fun searchSongs(
        source: String,
        keyword: String,
        limit: Int = 20,
    ): ApiResult<SearchResult>
}
