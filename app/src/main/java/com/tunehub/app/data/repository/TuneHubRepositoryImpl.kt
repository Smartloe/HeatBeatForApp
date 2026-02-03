package com.tunehub.app.data.repository

import com.tunehub.app.data.cache.InMemoryCache
import com.tunehub.app.data.mapper.toDomain
import com.tunehub.app.data.remote.NetworkClient
import com.tunehub.app.data.remote.TuneHubApiService
import com.tunehub.app.domain.model.ApiResult
import com.tunehub.app.domain.model.SearchResult
import com.tunehub.app.domain.model.SongInfo
import com.tunehub.app.domain.repository.TuneHubRepository
import retrofit2.HttpException
import java.io.IOException

class TuneHubRepositoryImpl(
    private val api: TuneHubApiService = NetworkClient.api,
    private val infoCache: InMemoryCache<String, SongInfo> = InMemoryCache(INFO_CACHE_TTL_MS),
    private val searchCache: InMemoryCache<String, SearchResult> = InMemoryCache(SEARCH_CACHE_TTL_MS),
) : TuneHubRepository {

    override suspend fun getSongInfo(source: String, id: String): ApiResult<SongInfo> {
        val cacheKey = "$source:$id"
        val cached = infoCache.get(cacheKey)
        if (cached != null) {
            return ApiResult.Success(cached)
        }

        return when (val result = safeCall { api.getSongInfo(source = source, id = id) }) {
            is ApiResult.Success -> {
                val mapped = result.data.toDomain()
                infoCache.put(cacheKey, mapped)
                ApiResult.Success(mapped)
            }

            is ApiResult.Error -> result
        }
    }

    override suspend fun searchSongs(
        source: String,
        keyword: String,
        limit: Int,
    ): ApiResult<SearchResult> {
        val cacheKey = "$source:$keyword:$limit"
        val cached = searchCache.get(cacheKey)
        if (cached != null) {
            return ApiResult.Success(cached)
        }

        return when (
            val result = safeCall {
                api.searchSongs(source = source, keyword = keyword, limit = limit)
            }
        ) {
            is ApiResult.Success -> {
                val mapped = result.data.toDomain()
                searchCache.put(cacheKey, mapped)
                ApiResult.Success(mapped)
            }

            is ApiResult.Error -> result
        }
    }

    private suspend fun <T> safeCall(call: suspend () -> com.tunehub.app.data.model.ApiResponse<T>): ApiResult<T> {
        return try {
            val response = call()
            if (response.code == 200 && response.data != null) {
                ApiResult.Success(response.data)
            } else {
                ApiResult.Error(
                    code = response.code,
                    message = mapErrorMessage(response.code, response.message),
                )
            }
        } catch (e: HttpException) {
            ApiResult.Error(
                code = e.code(),
                message = mapErrorMessage(e.code(), e.message()),
                cause = e,
            )
        } catch (e: IOException) {
            ApiResult.Error(message = "网络异常，请检查连接", cause = e)
        } catch (e: Exception) {
            ApiResult.Error(message = "请求失败，请稍后重试", cause = e)
        }
    }

    private fun mapErrorMessage(code: Int, fallback: String?): String {
        return when (code) {
            403 -> "访问受限，请检查 Referer/UA 或稍后重试"
            429 -> "请求过于频繁，请稍后重试"
            else -> fallback ?: "请求失败"
        }
    }

    companion object {
        private const val INFO_CACHE_TTL_MS = 5 * 60 * 1000L
        private const val SEARCH_CACHE_TTL_MS = 2 * 60 * 1000L
    }
}
