package com.magicpark.domain.usecases

import com.magicpark.domain.enums.PaymentMethod
import com.magicpark.domain.model.Order
import com.magicpark.domain.model.ShopItem
import com.magicpark.domain.repositories.IOrderRepository
import javax.inject.Inject

class OrderUseCases(@Inject var orderRepository: IOrderRepository) {

    suspend fun createOrder(
        shopItems: List<ShopItem>,
        paymentMethod: PaymentMethod = PaymentMethod.Orange,
        voucherCode: String?
    )  : Order =
        orderRepository.createOrder(
            paymentMethod = paymentMethod,
            voucherCode = voucherCode,
            shopItems = shopItems)

    suspend fun getOrder(orderId: Long): Order =
       orderRepository.getOrder(orderId)
}
