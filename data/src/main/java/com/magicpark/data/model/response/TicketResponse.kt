package com.magicpark.data.model.response
import com.magicpark.domain.model.UserTicket

data class TicketResponse(
    val ticket: UserTicket? = null
) : com.magicpark.data.model.base.ErrorResponse()
