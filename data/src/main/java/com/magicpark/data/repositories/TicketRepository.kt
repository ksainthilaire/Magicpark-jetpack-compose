package com.magicpark.data.repositories

import com.magicpark.data.api.MagicparkApi
import com.magicpark.data.local.UserTicketDao
import com.magicpark.domain.model.UserTicket
import com.magicpark.domain.repositories.ITicketRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent
import java.util.concurrent.TimeUnit

class TicketRepository : ITicketRepository {

    companion object {
        private val TAG = TicketRepository::class.java.simpleName
    }

    private val magicparkApi: MagicparkApi by KoinJavaComponent.inject(MagicparkApi::class.java)
    private val userTicketDao: UserTicketDao by KoinJavaComponent.inject(UserTicketDao::class.java)

    override suspend fun getWallet(userId: Long): List<UserTicket> {
        return withContext(Dispatchers.IO) {
            return@withContext userTicketDao
                .getUserTicket(userId)
        }
    }

    override suspend fun fetchWallet(userId: Long): List<UserTicket> {
        val response = magicparkApi.getWallet()

        val tickets = response.tickets ?: listOf()

        withContext(Dispatchers.IO) { userTicketDao.saveUserTickets(tickets) }

        return tickets
    }

    override suspend fun controlTicket(payload: String): Boolean =
        magicparkApi.controlTicket(payload)
            .blockingAwait(300L, TimeUnit.SECONDS)

}
