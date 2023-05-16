package com.magicpark.domain.usecases

import com.magicpark.domain.model.*
import com.magicpark.domain.repositories.ITicketRepository
import com.magicpark.domain.repositories.IUserRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.java.KoinJavaComponent.inject

class UserUseCases(val repository: IUserRepository){

   fun updateUser(
        mail: String?,
        fullName: String?,
        phoneNumber: String?,
        avatarUrl: String?,
        country: String?
    ) : Completable =
       repository.updateUser(mail, fullName, phoneNumber, avatarUrl, country)

    fun getUser(): Observable<User> = repository.getUser()
    fun deleteUser(): Completable = repository.deleteUser()
    fun login(token: String): Observable<User> = repository.login(token)

}