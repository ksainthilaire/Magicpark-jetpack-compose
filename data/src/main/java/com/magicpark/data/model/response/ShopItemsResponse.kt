package com.magicpark.data.model.response
import com.google.gson.annotations.SerializedName
import com.magicpark.domain.model.ShopCategory
import com.magicpark.domain.model.ShopItem

data class ShopItemsResponse(
    @SerializedName("shop_items")
    val shopItems: List<ShopItem>? = null,

    @SerializedName("shop_categories")
    val shopCategories: List<ShopCategory>? = null
)
