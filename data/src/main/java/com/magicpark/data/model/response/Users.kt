package com.magicpark.data.model.response
import com.google.gson.annotations.SerializedName
import com.magicpark.domain.model.User

data class UsersResponse(
    val users: List<User>? = null,

    @SerializedName("total_page")
    val totalPage: Int? = null,

    @SerializedName("page")
    val page: Int? = null
) : com.magicpark.data.model.base.ErrorResponse()
