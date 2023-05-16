package com.magicpark.data.repositories

import com.magicpark.data.api.MagicparkApi
import com.magicpark.data.model.request.authentification.LoginRequest
import com.magicpark.data.model.request.authentification.UpdateUserRequest
import com.magicpark.data.model.request.order.CreateOrderRequest
import com.magicpark.data.session.MagicparkDbSession
import com.magicpark.domain.enums.PaymentMethodEnum
import com.magicpark.domain.model.*
import com.magicpark.domain.repositories.IOrderRepository
import com.magicpark.domain.repositories.IUserRepository
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.schedulers.Schedulers

class UserRepository(private val magicparkApi: MagicparkApi, private val magicparkDbSession: MagicparkDbSession) : IUserRepository {

    override fun updateUser(
        mail: String?,
        fullname: String?,
        phoneNumber: String?,
        avatarUrl: String?,
        country: String?
    ) : Completable {
        val request = UpdateUserRequest(
            mail = mail,
            fullName = fullname,
            phoneNumber = phoneNumber,
            avatarUrl = avatarUrl,
            country = country
        )
        return magicparkApi.updateUser(magicparkDbSession.getToken(), request)
            .subscribeOn(Schedulers.io())

    }

    override fun deleteUser(): Completable {
        return magicparkApi.deleteUser(magicparkDbSession.getToken())
            .subscribeOn(Schedulers.io())
    }

    override fun getUser(): Observable<User> {
        return magicparkApi.getUser(magicparkDbSession.getToken())
            .subscribeOn(Schedulers.io())
            .map {
                it.user
            }
    }

    override fun login(token: String): Observable<User> {
        val request = LoginRequest(
            token = token
        )
        return magicparkApi.login(request)
            .subscribeOn(Schedulers.io()).map {
            it.user?.let(::saveToken)
        }
    }

    private fun saveToken(user: User): User {
        user.token?.let { token ->
            magicparkDbSession.saveToken(token)
        }
        return user
    }

}