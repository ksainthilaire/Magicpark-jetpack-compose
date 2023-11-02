package com.magicpark.data.model.response

import com.google.gson.annotations.SerializedName
import com.magicpark.domain.model.Payment

data class OrdersResponse(
    @SerializedName("orders")
    val orders: List<Payment>? = null,

    @SerializedName("total_page")
    val totalPage: Int? = null,

    @SerializedName("page")
    val page: Int? = null
) : com.magicpark.data.model.base.ErrorResponse()
