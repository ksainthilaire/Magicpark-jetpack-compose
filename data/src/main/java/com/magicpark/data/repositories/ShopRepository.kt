package com.magicpark.data.repositories

import com.magicpark.data.api.MagicparkApi
import com.magicpark.data.local.ShopDao
import com.magicpark.data.model.request.order.CreateOrderRequest
import com.magicpark.data.session.MagicparkDbSession
import com.magicpark.domain.enums.PaymentMethodEnum
import com.magicpark.domain.model.*
import com.magicpark.domain.repositories.IOrderRepository
import com.magicpark.domain.repositories.IShopRepository
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.schedulers.Schedulers

class ShopRepository(
    private val shopDao: ShopDao,
    private val magicparkApi: MagicparkApi,
    private val magicparkDbSession: MagicparkDbSession
) : IShopRepository {

    override fun getShopCategories(): Observable<ShopCategory> {
        TODO("ko")
        //return magicparkApi.getShopCategories()
    }

    override fun getShopItems(): Observable<List<ShopItem>> {
        return magicparkApi.getShopItems(magicparkDbSession.getToken())
            .subscribeOn(Schedulers.io())
            .map {
                it.shopItems
            }
    }
}