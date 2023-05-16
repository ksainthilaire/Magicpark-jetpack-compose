package com.magicpark.data.repositories

import com.magicpark.data.api.MagicparkApi
import com.magicpark.data.local.UserTicketDao
import com.magicpark.domain.model.User
import com.magicpark.domain.model.UserTicket
import com.magicpark.domain.repositories.ITicketRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class TicketRepository(
    private val userTicketDao: UserTicketDao,
    private val magicparkApi: MagicparkApi
) : ITicketRepository {

    override fun getWallet(): Observable<List<UserTicket>> {
        return magicparkApi.getWallet("")
            .subscribeOn(Schedulers.io())
            .map {
                it.tickets?.let {
                    saveTickets(it)
                    it
                } ?: listOf()
            }
    }

    override fun controlTicket(payload: String): Completable {
        return magicparkApi.controlTicket("", payload)
            .subscribeOn(Schedulers.io())
    }


    private fun getTicketsFromDatabase(): Observable<List<UserTicket>> =
        userTicketDao.getUserTickets()
            .subscribeOn(Schedulers.io())
            .onErrorReturnItem(listOf())

    private fun saveTickets(tickets: List<UserTicket>) =
        userTicketDao.saveUserTickets(tickets)

}