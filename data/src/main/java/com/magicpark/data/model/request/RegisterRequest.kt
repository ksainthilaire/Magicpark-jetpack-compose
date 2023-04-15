package com.magicpark.data.model.request

data class RegisterRequest(
    val fullName: String? = null,
    val mail: String? = null,
    val password: String? = null,
    val phoneNumber: String? = null,
    val country: String? = null
) : Request()