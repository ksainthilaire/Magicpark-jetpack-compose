package com.magicpark.data.model.response

import com.magicpark.domain.model.magicpark.ShopItem

data class ShopCategory(
    val id: Int,
    val name: String
)

typealias ShopItemResponse = List<ShopItem>