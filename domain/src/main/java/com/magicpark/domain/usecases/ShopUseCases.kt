package com.magicpark.domain.usecases

import com.magicpark.domain.model.Shop
import com.magicpark.domain.model.ShopCategory
import com.magicpark.domain.model.ShopItem
import com.magicpark.domain.repositories.IShopRepository
import javax.inject.Inject

class ShopUseCases(@Inject val repository: IShopRepository) {

    suspend fun fetchShopItems(): Shop =
        repository.getShopItemsFromRemote()

    suspend fun getShopItems(): Shop =
        repository.getShopItemsFromDatabase()

    suspend fun getShopCategories(): List<ShopCategory> =
        repository.getShopCategories()

    suspend fun addFavorite(shopItem: ShopItem) =
        repository.addFavorite(shopItem)

    suspend fun removeFavorite(shopItem: ShopItem) =
        repository.removeFavorite(shopItem)
}
