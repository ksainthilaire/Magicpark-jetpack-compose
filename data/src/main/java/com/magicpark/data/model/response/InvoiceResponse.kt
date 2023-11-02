package com.magicpark.data.model.response

import com.magicpark.domain.model.Invoice

data class InvoiceResponse(
    val invoice: Invoice? = null
) : com.magicpark.data.model.base.ErrorResponse()
