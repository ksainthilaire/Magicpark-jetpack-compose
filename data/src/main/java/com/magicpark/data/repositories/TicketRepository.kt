package com.magicpark.data.repositories

import com.magicpark.data.api.MagicparkApi
import com.magicpark.data.local.UserTicketDao
import com.magicpark.domain.model.UserTicket
import com.magicpark.domain.repositories.ITicketRepository
import kotlinx.coroutines.flow.single
import org.koin.java.KoinJavaComponent
import java.util.concurrent.TimeUnit

class TicketRepository : ITicketRepository {

    companion object {
        private val TAG = TicketRepository::class.java.simpleName
    }

    private val magicparkApi: MagicparkApi by KoinJavaComponent.inject(MagicparkApi::class.java)
    private val userTicketDao: UserTicketDao by KoinJavaComponent.inject(UserTicketDao::class.java)

    override suspend fun getWallet(): List<UserTicket> {
        val response = magicparkApi.getWallet()
            .single()

        val tickets = response.tickets ?: listOf()
        saveTickets(tickets)

        return tickets
    }

    override suspend fun controlTicket(payload: String): Boolean =
        magicparkApi.controlTicket(payload)
            .blockingAwait(300L, TimeUnit.SECONDS)

    suspend fun getTicketsFromDatabase(): List<UserTicket> =
        userTicketDao.getUserTickets()
            .single()

    private fun saveTickets(tickets: List<UserTicket>) =
        userTicketDao.saveUserTickets(tickets)
}
