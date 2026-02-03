package com.tunehub.app.data.model

data class ApiResponse<T>(
    val code: Int,
    val message: String? = null,
    val data: T? = null,
    val timestamp: String? = null,
)
