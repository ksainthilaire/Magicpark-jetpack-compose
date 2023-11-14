package com.magicpark.data.repositories

import com.magicpark.data.api.MagicparkApi
import com.magicpark.data.model.request.toShopItemRequest
import com.magicpark.data.model.request.order.CreateOrderRequest
import com.magicpark.domain.enums.PaymentMethod
import com.magicpark.domain.model.*
import com.magicpark.domain.repositories.IOrderRepository
import org.koin.java.KoinJavaComponent

class OrderRepository : IOrderRepository {

    private val api: MagicparkApi by KoinJavaComponent.inject(MagicparkApi::class.java)

    override suspend fun createOrder(
        paymentMethod: PaymentMethod,
        voucherCode: String?,
        shopItems: List<ShopItem>,
    ): Order {

        val request = CreateOrderRequest(
            items = shopItems.map { shopItem -> shopItem.toShopItemRequest() },
            voucherCode = voucherCode,
            paymentMethod = paymentMethod
        )

        val response = api.createOrder(request)

        return response.order ?: throw Exception("The order is empty")
    }

    override suspend fun getOrder(orderId: Long): Order {
        val response = api
            .getOrder(orderId.toString())

        return response.order ?: throw Exception("The order field is empty")
    }

    override suspend fun getPayment(orderId: Long): Payment {
        val response = api
            .getPayment(orderId.toString())

        return response.payment ?: throw Exception("The payment field is empty")
    }

    override suspend fun getPaymentInvoices(): List<Invoice> {
        val response = api
            .getInvoices()

        return response.invoices ?: throw Exception("The invoices field is empty")
    }
}
