package com.magicpark.data.repositories

import com.magicpark.data.api.MagicparkApi
import com.magicpark.data.model.request.authentification.LoginRequest
import com.magicpark.data.model.request.authentification.UpdateUserRequest
import com.magicpark.data.model.request.order.CreateOrderRequest
import com.magicpark.domain.enums.PaymentMethodEnum
import com.magicpark.domain.model.*
import com.magicpark.domain.repositories.IOrderRepository
import com.magicpark.domain.repositories.IUserRepository
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.schedulers.Schedulers

class UserRepository(private val magicparkApi: MagicparkApi) : IUserRepository {

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
        return magicparkApi.updateUser("", request)
            .subscribeOn(Schedulers.io())

    }

    override fun deleteUser(): Completable {
        return magicparkApi.deleteUser("")
            .subscribeOn(Schedulers.io())
    }

    override fun getUser(): Observable<User> {
        return magicparkApi.getUser("")
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
            it.user
        }
    }

}