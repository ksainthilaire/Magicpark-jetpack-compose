package com.magicpark.domain.usecases

import androidx.appcompat.app.AppCompatActivity
import com.magicpark.domain.model.*
import com.magicpark.domain.repositories.ITicketRepository
import com.magicpark.domain.repositories.IUserRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.java.KoinJavaComponent.inject
import javax.inject.Inject

class UserUseCases @Inject constructor(var repository: IUserRepository) {

    fun updateUser(
        mail: String?,
        fullName: String?,
        phoneNumber: String?,
        avatarUrl: String?,
        country: String?
    ): Completable =
        repository.updateUser(mail, fullName, phoneNumber, avatarUrl, country)

    fun getUser(): Observable<User> = repository.getUser()
    fun deleteUser(): Completable = repository.deleteUser()

    fun loginWithFacebook(activity: AppCompatActivity): Observable<User> = repository.loginWithFacebook(activity)
    fun loginWithGoogle(activity: AppCompatActivity): Observable<User> = repository.loginWithGoogle(activity)
    fun loginWithMail(activity: AppCompatActivity, mail: String, password: String): Observable<User> =
        repository.loginWithMail(activity, mail, password)

    fun registerWithMail(
        activity: AppCompatActivity,
        fullName: String,
        phoneNumber: String,
        mail: String,
        password: String
    ): Observable<User> =
        repository.registerWithMail(activity, fullName, phoneNumber, mail, password)

    fun forgot(mail: String) : Completable = repository.forgot(mail)

    fun logout() : Completable = repository.logout()

}