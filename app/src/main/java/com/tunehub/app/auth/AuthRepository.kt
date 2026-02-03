package com.tunehub.app.auth

import com.tunehub.app.domain.model.ApiResult

interface AuthRepository {
    suspend fun fetchCaptcha(): ApiResult<CaptchaDto>

    suspend fun fetchPublicKey(): ApiResult<PublicKeyDto>

    suspend fun register(
        username: String,
        password: String,
        captchaId: String,
        captchaCode: String,
    ): ApiResult<String>

    suspend fun login(
        username: String,
        password: String,
        captchaId: String,
        captchaCode: String,
    ): ApiResult<AuthToken>

    suspend fun refreshIfNeeded(): ApiResult<AuthToken>

    suspend fun logout()
}
