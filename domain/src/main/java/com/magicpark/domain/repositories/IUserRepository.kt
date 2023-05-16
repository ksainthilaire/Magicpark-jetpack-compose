package com.magicpark.domain.repositories

import com.magicpark.domain.model.User
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface IUserRepository {

    fun updateUser(
        mail: String? = null,
        fullname: String? = null,
        phoneNumber: String? = null,
        avatarUrl: String? = null,
        country: String? = null
    ) : Completable

    fun deleteUser(): Completable

    fun getUser(): Observable<User>

    fun login(token: String): Observable<User>
}