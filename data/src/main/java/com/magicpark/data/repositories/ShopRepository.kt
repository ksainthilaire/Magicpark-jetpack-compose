package com.magicpark.data.repositories

import com.magicpark.data.api.MagicparkApi
import com.magicpark.domain.model.*
import com.magicpark.domain.repositories.IShopRepository
import kotlinx.coroutines.flow.single
import org.koin.java.KoinJavaComponent

class ShopRepository : IShopRepository {

    private val api: MagicparkApi by KoinJavaComponent.inject(MagicparkApi::class.java)

    /**
     * TODO: Fix
     * private val shopDao: ShopDao by KoinJavaComponent.inject(ShopDao::class.java)
     */

    override suspend fun getShopCategories(): List<ShopCategory> {
        val response = api
            .getShopCategories()
            .single()

        return response.categories ?: emptyList()
    }

    override suspend fun getShopItems(): Shop {
        val response = api
            .getShopItems()
            .single()

        val items = response.shopItems ?: emptyList()
        val categories = response.shopCategories ?: emptyList()

        return Pair(
            items,
            categories
        )
    }
}
