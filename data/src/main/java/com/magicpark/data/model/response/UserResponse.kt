package com.magicpark.data.model.response
import com.magicpark.domain.model.User

data class UserResponse(
    val user: User? = null
) : com.magicpark.data.model.base.ErrorResponse()
