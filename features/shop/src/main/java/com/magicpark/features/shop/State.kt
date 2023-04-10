package com.magicpark.features.shop

import com.magicpark.domain.model.Movie
import com.magicpark.domain.model.magicpark.ShopItem

sealed class ShopState {


    data class Categories(val categories: List<ShopCategory>? = null) : ShopState()

    data class ShopList(val shopItems: List<ShopItem>? = null) : ShopState()

}