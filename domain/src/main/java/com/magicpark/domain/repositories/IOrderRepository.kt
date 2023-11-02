package com.magicpark.domain.repositories

import com.magicpark.domain.enums.PaymentMethod
import com.magicpark.domain.model.*

interface IOrderRepository {

    suspend fun createOrder(
        paymentMethod: PaymentMethod = PaymentMethod.Orange,
        voucherCode: String?,
        shopItems: List<ShopItem>,
    ): Order

    suspend fun getOrder(orderId: Long): Order

    suspend fun getPayment(orderId: Long): Payment

    suspend fun getPaymentInvoice(orderId: Long): Invoice

}
