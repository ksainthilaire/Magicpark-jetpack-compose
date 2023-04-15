package com.magicpark.data.model.response

import com.magicpark.domain.model.magicpark.User

data class UserResponse(
    val user: User? = null
) : Response()