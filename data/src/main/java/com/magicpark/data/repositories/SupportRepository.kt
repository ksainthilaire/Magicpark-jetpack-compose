package com.magicpark.data.repositories

import com.magicpark.data.api.MagicparkApi
import com.magicpark.data.local.ShopDao
import com.magicpark.data.model.request.HelpRequest
import com.magicpark.data.model.request.order.CreateOrderRequest
import com.magicpark.data.session.MagicparkDbSession
import com.magicpark.domain.enums.PaymentMethodEnum
import com.magicpark.domain.model.*
import com.magicpark.domain.repositories.IOrderRepository
import com.magicpark.domain.repositories.IShopRepository
import com.magicpark.domain.repositories.ISupportRepository
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.java.KoinJavaComponent
import javax.inject.Inject

class SupportRepository : ISupportRepository {


    private val magicparkDbSession: MagicparkDbSession by KoinJavaComponent.inject(
        MagicparkDbSession::class.java
    )
    private val magicparkApi: MagicparkApi by KoinJavaComponent.inject(MagicparkApi::class.java)

    override fun help(message: String): Completable {
        val request = HelpRequest(
            message
        )
        return magicparkApi.help(magicparkDbSession.getToken(), request)
            .subscribeOn(Schedulers.io())
    }

}