package com.magicpark.data.model.response
import com.magicpark.domain.model.UserTicket


data class WalletResponse(
    val tickets: List<UserTicket>? = null
)
