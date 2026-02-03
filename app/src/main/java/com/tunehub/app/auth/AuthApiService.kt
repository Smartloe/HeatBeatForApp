package com.tunehub.app.auth

import com.tunehub.app.data.model.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {
    @GET("auth/captcha")
    suspend fun getCaptcha(): ApiResponse<CaptchaDto>

    @GET("auth/public-key")
    suspend fun getPublicKey(): ApiResponse<PublicKeyDto>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): ApiResponse<RegisterResponse>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): ApiResponse<LoginResponse>
}
