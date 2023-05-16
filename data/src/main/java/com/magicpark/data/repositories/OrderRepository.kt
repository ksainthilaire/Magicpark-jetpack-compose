package com.magicpark.data.repositories

import com.magicpark.data.api.MagicparkApi
import com.magicpark.data.model.request.order.CreateOrderRequest
import com.magicpark.domain.enums.PaymentMethodEnum
import com.magicpark.domain.model.*
import com.magicpark.domain.repositories.IOrderRepository
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.schedulers.Schedulers

class OrderRepository(private val magicparkApi: MagicparkApi) : IOrderRepository {

    override fun createOrder(
        shopItems: List<ShopItem>,
        paymentMethod: PaymentMethodEnum,
        voucherCode: String?
    ): Observable<Order> {

        val request = CreateOrderRequest(
            items = shopItems,
            voucherCode = voucherCode,
            paymentMethod = paymentMethod
        )

        return magicparkApi.createOrder("", request)
            .subscribeOn(Schedulers.io())
            .map {
                it.order
            }

    }

    override fun getOrder(orderId: Long): Observable<Order> {
        return magicparkApi.getOrder("", orderId.toString())
            .subscribeOn(Schedulers.io())
            .map {
            it.order
        }
    }
}