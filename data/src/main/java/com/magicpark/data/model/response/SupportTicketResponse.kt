package com.magicpark.data.model.response

import com.magicpark.domain.model.Support

data class SupportTicketResponse(
    val ticket: Support
) : com.magicpark.data.model.base.ErrorResponse()