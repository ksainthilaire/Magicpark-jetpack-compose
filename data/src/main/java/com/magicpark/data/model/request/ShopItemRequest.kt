package com.magicpark.data.model.request

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import com.magicpark.domain.model.ShopItem

data class ShopItemRequest(
    @SerializedName("id")
    val id: Long? = null,

    @ColumnInfo(name="quantity")
    var quantity: Int? = 0,
)

fun ShopItem.toShopItemRequest() : ShopItemRequest =
    ShopItemRequest(id = id, quantity = quantity)
