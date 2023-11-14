package com.magicpark.domain.usecases

import com.magicpark.domain.model.UserTicket
import com.magicpark.domain.repositories.ITicketRepository
import javax.inject.Inject

class TicketUseCases(@Inject val repository: ITicketRepository){
    suspend fun getWallet(userId: Long): List<UserTicket> =
        repository.getWallet(userId)

    suspend fun fetchWallet(userId: Long): List<UserTicket> =
        repository.fetchWallet(userId)

    suspend fun controlTicket(payload: String) : Boolean =
        repository.controlTicket(payload)
}
