package com.magicpark.domain.repositories

import com.magicpark.domain.model.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface IShopRepository {
    suspend fun getShopCategories(): List<ShopCategory>

    suspend fun getShopItemsFromDatabase(): Shop

    suspend fun getShopItemsFromRemote(): Shop

    suspend fun addFavorite(shopItem: ShopItem)

    suspend fun removeFavorite(shopItem: ShopItem)
}
