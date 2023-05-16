package com.magicpark.data.model.response

import com.google.gson.annotations.SerializedName
import com.magicpark.domain.model.Support

data class SupportTicketsResponse(
    val ticket: List<Support>? = null,

    @SerializedName("total_page")
    val totalPage: Int? = null,

    @SerializedName("page")
    val page: Int? = null
) : com.magicpark.data.model.base.ErrorResponse()