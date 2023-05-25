package com.magicpark.features.shop

import com.magicpark.domain.model.ShopItem



sealed class ShopState  {
    /* Login */
    class onLoaded(
        val shopItem: List<ShopItem>,
        val categories: List<String>) : ShopState()



}