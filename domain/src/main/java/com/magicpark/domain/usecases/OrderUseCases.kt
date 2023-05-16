package com.magicpark.domain.usecases

import com.magicpark.domain.enums.PaymentMethodEnum
import com.magicpark.domain.model.Order
import com.magicpark.domain.model.ShopItem
import com.magicpark.domain.repositories.IOrderRepository
import com.magicpark.domain.repositories.IUserRepository
import io.reactivex.rxjava3.core.Observable
import org.koin.java.KoinJavaComponent.inject

class OrderUseCases(private val orderRepository: IOrderRepository) {


    fun createOrder(
        shopItem: List<ShopItem>,
        paymentMethod: PaymentMethodEnum = PaymentMethodEnum.Orange,
        voucherCode: String?
    )  : Observable<Order> {
       return orderRepository.createOrder(shopItem, paymentMethod, voucherCode)
    }

    fun getOrder(orderId: Long): Observable<Order> {
       return orderRepository.getOrder(orderId)
    }
}