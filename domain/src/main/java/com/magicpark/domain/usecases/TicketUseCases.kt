package com.magicpark.domain.usecases

import com.magicpark.domain.model.UserTicket
import com.magicpark.domain.repositories.ISupportRepository
import com.magicpark.domain.repositories.ITicketRepository
import com.magicpark.domain.repositories.IUserRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import org.koin.java.KoinJavaComponent.inject
import javax.inject.Inject

class TicketUseCases(  @Inject val repository: ITicketRepository){

    fun getWallet(): Observable<List<UserTicket>> {
        return repository.getWallet()
    }

    fun controlTicket(payload: String) : Completable =
        repository.controlTicket(payload)
}