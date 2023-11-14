package com.magicpark.domain.repositories

import com.magicpark.domain.model.*

interface ITicketRepository {

    suspend fun controlTicket(payload: String): Boolean

    suspend fun getWallet(userId: Long): List<UserTicket>

    suspend fun fetchWallet(userId: Long): List<UserTicket>
}
