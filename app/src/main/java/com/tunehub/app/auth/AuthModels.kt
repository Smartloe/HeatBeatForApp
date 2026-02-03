package com.tunehub.app.auth

data class CaptchaDto(
    val captcha_id: String,
    val image: String,
    val expires_in: Long,
)

data class PublicKeyDto(
    val key_id: String,
    val public_key: String,
    val expires_in: Long,
)

data class RegisterRequest(
    val username: String,
    val password: String,
    val key_id: String,
    val captcha_id: String,
    val captcha_code: String,
    val nonce: String,
    val timestamp: Long,
)

data class RegisterResponse(
    val user_id: String,
)

data class LoginRequest(
    val username: String,
    val password: String,
    val key_id: String,
    val captcha_id: String,
    val captcha_code: String,
    val nonce: String,
    val timestamp: Long,
)

data class LoginResponse(
    val token: String,
    val expires_in: Long,
)
