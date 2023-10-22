package com.magicpark.data.repositories

import com.magicpark.data.api.MagicparkApi
import com.magicpark.data.model.request.order.CreateOrderRequest
import com.magicpark.domain.enums.PaymentMethod
import com.magicpark.domain.model.*
import com.magicpark.domain.repositories.IOrderRepository
import kotlinx.coroutines.flow.single
import org.koin.java.KoinJavaComponent

class OrderRepository : IOrderRepository {

    private val api: MagicparkApi by KoinJavaComponent.inject(MagicparkApi::class.java)

    override suspend fun createOrder(
        paymentMethod: PaymentMethod,
        voucherCode: String?,
        shopItems: List<ShopItem>,
    ): Order {

        val request = CreateOrderRequest(
            items = shopItems,
            voucherCode = voucherCode,
            paymentMethod = paymentMethod
        )

        val response = api.createOrder(request)
            .single()

        return response.order ?: throw Exception("The order is empty")
    }

    override suspend fun getOrder(orderId: Long): Order {
        val response = api
            .getOrder(orderId.toString())
            .single()

        return response.order ?: throw Exception("The order is empty")
    }
}
