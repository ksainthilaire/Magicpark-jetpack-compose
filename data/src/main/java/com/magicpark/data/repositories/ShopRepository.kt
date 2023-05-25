package com.magicpark.data.repositories

import com.magicpark.data.api.MagicparkApi
import com.magicpark.data.local.ShopDao
import com.magicpark.data.model.request.order.CreateOrderRequest
import com.magicpark.data.session.MagicparkDbSession
import com.magicpark.domain.enums.PaymentMethodEnum
import com.magicpark.domain.model.*
import com.magicpark.domain.repositories.IOrderRepository
import com.magicpark.domain.repositories.IShopRepository
import com.magicpark.domain.usecases.UserUseCases
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.java.KoinJavaComponent
import javax.inject.Inject

class ShopRepository : IShopRepository {

    private val magicparkDbSession: MagicparkDbSession by KoinJavaComponent.inject(
        MagicparkDbSession::class.java
    )
    private val magicparkApi: MagicparkApi by KoinJavaComponent.inject(MagicparkApi::class.java)
    private val shopDao: ShopDao by KoinJavaComponent.inject(ShopDao::class.java)


    override fun getShopCategories(): Observable<ShopCategory> {
        TODO("ko")
        //return magicparkApi.getShopCategories()
    }

    override fun getShopItems(): Observable<Shop> {
        return magicparkApi.getShopItems(magicparkDbSession.getToken())
            .subscribeOn(Schedulers.io())
            .map {
                val shopItems = it.shopItems ?: listOf()
                val shopCategories = it.shopCategories ?: listOf()

                Pair(shopItems, shopCategories)
            }
    }

    override fun addProduct(shopItem: ShopItem): Completable {
       return Completable.create {
           magicparkDbSession.addProduct(shopItem)
           it.onComplete()
       }
    }

    override fun removeProduct(shopItem: ShopItem): Completable {
        return Completable.create {
            magicparkDbSession.removeProduct(shopItem)
            it.onComplete()
        }
    }

    override fun getProducts(): Observable<Cart> {
        return Observable.create {
            val products = magicparkDbSession.getProducts()
            it.onNext(products)
        }
    }

    override fun clearCart(): Completable {
        return Completable.create {
            magicparkDbSession.clearCart()
            it.onComplete()
        }
    }

    override fun getTotal(): Observable<Float> {
        return Observable.create {
            val total = magicparkDbSession.getTotal()
            it.onNext(total)
        }
    }


}