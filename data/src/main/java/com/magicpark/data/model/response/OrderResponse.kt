package com.magicpark.data.model.response

import com.magicpark.domain.model.Order

data class OrderResponse(
    val order: Order? = null
) : com.magicpark.data.model.base.ErrorResponse()
