package com.magicpark.domain.usecases

import com.magicpark.domain.model.UserTicket
import com.magicpark.domain.repositories.ITicketRepository
import javax.inject.Inject

class TicketUseCases(@Inject val repository: ITicketRepository){
    suspend fun getWallet(): List<UserTicket> =
        repository.getWallet()

    suspend fun controlTicket(payload: String) : Boolean =
        repository.controlTicket(payload)
}
