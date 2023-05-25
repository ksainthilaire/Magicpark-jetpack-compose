package com.magicpark.domain.repositories

import androidx.appcompat.app.AppCompatActivity
import com.magicpark.domain.model.User
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface IUserRepository {

    fun logout() : Completable

    fun updateUser(
        mail: String? = null,
        fullname: String? = null,
        phoneNumber: String? = null,
        avatarUrl: String? = null,
        country: String? = null
    ): Completable

    fun deleteUser(): Completable

    fun getUser(): Observable<User>
    fun forgot(mail: String): Completable



    fun loginWithFacebook(activity: AppCompatActivity): Observable<User>
    fun loginWithGoogle(activity: AppCompatActivity): Observable<User>
    fun loginWithMail(activity: AppCompatActivity, mail: String, password: String): Observable<User>
    fun registerWithMail(activity: AppCompatActivity, fullName: String, phoneNumber: String, mail: String, password: String): Observable<User>
}