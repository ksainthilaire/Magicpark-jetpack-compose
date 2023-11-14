package com.magicpark.data.repositories

import com.magicpark.data.api.MagicparkApi
import com.magicpark.data.local.ShopDao
import com.magicpark.domain.model.*
import com.magicpark.domain.repositories.IShopRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent

class ShopRepository : IShopRepository {

    private val api: MagicparkApi by KoinJavaComponent.inject(MagicparkApi::class.java)
    private val shopDao: ShopDao by KoinJavaComponent.inject(ShopDao::class.java)

    override suspend fun getShopCategories(): List<ShopCategory> {

        val response = api
            .getShopCategories()

        return response.categories ?: emptyList()
    }

    override suspend fun addFavorite(shopItem: ShopItem) {
        withContext(Dispatchers.IO) {

            println("addFavorite(${shopItem.apply { isFavorite = true }})")

            shopDao
                .saveShopItem(shopItem.apply { isFavorite = true })
        }
    }

    override suspend fun removeFavorite(shopItem: ShopItem) {
        withContext(Dispatchers.IO) {
            println("removeFavorite($shopItem)")
            shopDao
                .saveShopItem(shopItem.apply { isFavorite = false })
        }
    }

    override suspend fun getShopItemsFromDatabase(): Shop =
        getShopItems(fetchFromApi = false)

    override suspend fun getShopItemsFromRemote(): Shop =
        getShopItems(fetchFromApi = true)

    private suspend fun getShopItems(fetchFromApi: Boolean): Shop =
        withContext(Dispatchers.IO) {
            if (!fetchFromApi) {
                val savedShopItems = shopDao.getShopItems()
                val savedShopCategories = shopDao.getShopCategories()

                if (savedShopItems.isNotEmpty().and(savedShopCategories.isNotEmpty())) {
                    return@withContext Pair(first = savedShopItems, second = savedShopCategories)
                }
            }

        val response = api.getShopItems()
        val shopCategories = response.shopCategories ?: emptyList()
        val shopItems = response.shopItems ?: emptyList()

        shopDao.saveShopItems(shopItems)
        shopDao.saveShopCategories(shopCategories)

        return@withContext Pair(
            shopItems,
            shopCategories,
        )
    }
}
