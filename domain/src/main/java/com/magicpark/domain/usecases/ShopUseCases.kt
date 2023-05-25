package com.magicpark.domain.usecases

import com.magicpark.domain.model.Cart
import com.magicpark.domain.model.Shop
import com.magicpark.domain.model.ShopCategory
import com.magicpark.domain.model.ShopItem
import com.magicpark.domain.repositories.IShopRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class ShopUseCases(@Inject val repository: IShopRepository) {

    fun getShopItems(): Observable<Shop> = repository.getShopItems()
    fun getShopCategories(): Observable<ShopCategory> = repository.getShopCategories()

    fun addProduct(shopItem: ShopItem): Completable = repository.addProduct(shopItem)
    fun removeProduct(shopItem: ShopItem): Completable = repository.removeProduct(shopItem)
    fun getProducts(): Observable<Cart> = repository.getProducts()
    fun clearCart(): Completable = repository.clearCart()
    fun getTotal(): Observable<Float> = repository.getTotal()
    
}