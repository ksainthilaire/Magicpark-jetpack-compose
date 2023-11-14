package com.magicpark.data.model.response

import com.magicpark.domain.model.Invoice

data class InvoiceResponse(
    val invoices: List<Invoice>? = null,
) : com.magicpark.data.model.base.ErrorResponse()
