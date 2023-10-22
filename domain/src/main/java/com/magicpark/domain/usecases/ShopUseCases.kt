package com.magicpark.domain.usecases

import com.magicpark.domain.model.Shop
import com.magicpark.domain.model.ShopCategory
import com.magicpark.domain.repositories.IShopRepository
import javax.inject.Inject

class ShopUseCases(@Inject val repository: IShopRepository) {

    suspend fun getShopItems(): Shop =
        repository.getShopItems()

    suspend fun getShopCategories(): List<ShopCategory> =
        repository.getShopCategories()
}
