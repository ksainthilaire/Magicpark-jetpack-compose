package com.magicpark.data.model.request

data class UpdateUserRequest(
    val fullName: String? = null,
    val password: String? = null,
    val phoneNumber: String? = null,
    val country: String? = null
) : Request()