package com.magicpark.data.repositories

import com.magicpark.data.api.MagicparkApi
import com.magicpark.data.local.UserTicketDao
import com.magicpark.data.session.MagicparkDbSession
import com.magicpark.domain.model.User
import com.magicpark.domain.model.UserTicket
import com.magicpark.domain.repositories.ITicketRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.java.KoinJavaComponent
import javax.inject.Inject

class TicketRepository : ITicketRepository {


    private val magicparkDbSession: MagicparkDbSession by KoinJavaComponent.inject(
        MagicparkDbSession::class.java
    )
    private val magicparkApi: MagicparkApi by KoinJavaComponent.inject(MagicparkApi::class.java)
    private val userTicketDao: UserTicketDao by KoinJavaComponent.inject(UserTicketDao::class.java)

    override fun getWallet(): Observable<List<UserTicket>> {
        return magicparkApi.getWallet(magicparkDbSession.getToken())
            .subscribeOn(Schedulers.io())
            .map {
                it.tickets?.let {
                    saveTickets(it)
                    it
                } ?: listOf()
            }
    }

    override fun controlTicket(payload: String): Completable {
        return magicparkApi.controlTicket(magicparkDbSession.getToken(), payload)
            .subscribeOn(Schedulers.io())
    }


    private fun getTicketsFromDatabase(): Observable<List<UserTicket>> =
        userTicketDao.getUserTickets()
            .subscribeOn(Schedulers.io())
            .onErrorReturnItem(listOf())

    private fun saveTickets(tickets: List<UserTicket>) =
        userTicketDao.saveUserTickets(tickets)

}