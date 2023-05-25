package com.magicpark.data.model.response
import com.google.gson.annotations.SerializedName
import com.magicpark.domain.model.ShopCategory
import com.magicpark.domain.model.ShopItem

data class ShopItemResponse(
    @SerializedName("shop_item")
    val shopItems: ShopItem? = null,

    @SerializedName("shop_categories")
    val categories: List<ShopCategory>? = null
) : com.magicpark.data.model.base.ErrorResponse()
