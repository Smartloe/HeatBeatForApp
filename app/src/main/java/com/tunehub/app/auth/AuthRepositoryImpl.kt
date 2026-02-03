package com.tunehub.app.auth

import com.tunehub.app.data.model.ApiResponse
import com.tunehub.app.data.remote.NetworkClient
import com.tunehub.app.domain.model.ApiResult
import kotlinx.coroutines.flow.firstOrNull
import retrofit2.HttpException
import java.io.IOException
import java.security.SecureRandom

class AuthRepositoryImpl(
    private val api: AuthApiService = NetworkClient.create(AuthApiService::class.java),
    private val tokenStore: TokenStore,
) : AuthRepository {

    override suspend fun fetchCaptcha(): ApiResult<CaptchaDto> {
        return safeCall { api.getCaptcha() }
    }

    override suspend fun fetchPublicKey(): ApiResult<PublicKeyDto> {
        return safeCall { api.getPublicKey() }
    }

    override suspend fun register(
        username: String,
        password: String,
        captchaId: String,
        captchaCode: String,
    ): ApiResult<String> {
        val keyResult = fetchPublicKey()
        if (keyResult is ApiResult.Error) return keyResult
        val key = (keyResult as ApiResult.Success).data

        val nonce = generateNonce()
        val timestamp = System.currentTimeMillis()
        val encrypted = AuthCrypto.encryptPassword(password, nonce, timestamp, key.public_key)
        val request = RegisterRequest(
            username = username,
            password = encrypted,
            key_id = key.key_id,
            captcha_id = captchaId,
            captcha_code = captchaCode,
            nonce = nonce,
            timestamp = timestamp,
        )
        val result = safeCall { api.register(request) }
        return when (result) {
            is ApiResult.Success -> ApiResult.Success(result.data.user_id)
            is ApiResult.Error -> result
        }
    }

    override suspend fun login(
        username: String,
        password: String,
        captchaId: String,
        captchaCode: String,
    ): ApiResult<AuthToken> {
        val keyResult = fetchPublicKey()
        if (keyResult is ApiResult.Error) return keyResult
        val key = (keyResult as ApiResult.Success).data

        val nonce = generateNonce()
        val timestamp = System.currentTimeMillis()
        val encrypted = AuthCrypto.encryptPassword(password, nonce, timestamp, key.public_key)
        val request = LoginRequest(
            username = username,
            password = encrypted,
            key_id = key.key_id,
            captcha_id = captchaId,
            captcha_code = captchaCode,
            nonce = nonce,
            timestamp = timestamp,
        )
        val result = safeCall { api.login(request) }
        return when (result) {
            is ApiResult.Success -> {
                val response = result.data
                tokenStore.saveToken(response.token, response.expires_in)
                ApiResult.Success(AuthToken(response.token, System.currentTimeMillis() + response.expires_in * 1000))
            }
            is ApiResult.Error -> result
        }
    }

    override suspend fun refreshIfNeeded(): ApiResult<AuthToken> {
        val token = tokenStore.tokenFlow.firstOrNull() ?: return ApiResult.Error(message = "未登录")
        return if (token.isExpired()) {
            ApiResult.Error(message = "登录已过期，请重新登录")
        } else {
            ApiResult.Success(token)
        }
    }

    override suspend fun logout() {
        tokenStore.clear()
    }

    private suspend fun <T> safeCall(call: suspend () -> ApiResponse<T>): ApiResult<T> {
        return try {
            val response = call()
            if (response.code == 200 && response.data != null) {
                ApiResult.Success(response.data)
            } else {
                ApiResult.Error(code = response.code, message = response.message ?: "认证失败")
            }
        } catch (e: HttpException) {
            ApiResult.Error(code = e.code(), message = e.message() ?: "认证失败", cause = e)
        } catch (e: IOException) {
            ApiResult.Error(message = "网络异常，请检查连接", cause = e)
        } catch (e: Exception) {
            ApiResult.Error(message = "请求失败，请稍后重试", cause = e)
        }
    }

    private fun generateNonce(): String {
        val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        val random = SecureRandom()
        return (1..16)
            .map { chars[random.nextInt(chars.length)] }
            .joinToString("")
    }
}
