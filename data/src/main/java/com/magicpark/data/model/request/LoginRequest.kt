package com.magicpark.data.model.request

data class LoginRequest(
    val token: String? = null
) : Request()