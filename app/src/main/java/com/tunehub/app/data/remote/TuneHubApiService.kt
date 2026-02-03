package com.tunehub.app.data.remote

import com.tunehub.app.data.model.ApiResponse
import com.tunehub.app.data.model.SearchResultDto
import com.tunehub.app.data.model.SongInfoDto
import retrofit2.http.GET
import retrofit2.http.Query

interface TuneHubApiService {
    @GET("api/")
    suspend fun getSongInfo(
        @Query("source") source: String,
        @Query("id") id: String,
        @Query("type") type: String = "info",
    ): ApiResponse<SongInfoDto>

    @GET("api/")
    suspend fun searchSongs(
        @Query("source") source: String,
        @Query("type") type: String = "search",
        @Query("keyword") keyword: String,
        @Query("limit") limit: Int = 20,
    ): ApiResponse<SearchResultDto>
}
