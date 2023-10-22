package com.magicpark.domain.repositories

import com.magicpark.domain.model.*

interface ITicketRepository {

    suspend fun getWallet() : List<UserTicket>

    suspend fun controlTicket(payload: String): Boolean
}
