package com.magicpark.data.model.response

import com.magicpark.domain.model.Invoice
import com.magicpark.domain.model.Payment

data class PaymentResponse(
    val payment: Payment? = null
) : com.magicpark.data.model.base.ErrorResponse()
