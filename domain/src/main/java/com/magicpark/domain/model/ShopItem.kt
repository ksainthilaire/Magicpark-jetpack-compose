package com.magicpark.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

@Entity(tableName = "shop")
data class ShopItem(
    @PrimaryKey var id: Long? = null,
    @ColumnInfo(name="name")
    val name: String? = null,

    @ColumnInfo(name="description")
    var description: String? = null,

    @ColumnInfo(name="image_url")
    @SerializedName("image_url")
    var imageUrl: String? = null,

    @ColumnInfo(name="background_color")
    @SerializedName("background_color")
    var backgroundColor: String? = null,

    @ColumnInfo(name="categories")
    @SerializedName("categories")
    var categories: String? = null,

    @ColumnInfo(name="price")
    var price: Float? = null,


    @ColumnInfo(name="promotional_price")
    @SerializedName("promotional_price")
    var promotionalPrice: Float? = null,


    @ColumnInfo(name="quantity")
    var quantity: Int? = null,

    @ColumnInfo(name="is_pack")
    @SerializedName("is_pack")
    var isPack: Boolean? = false,

    @ColumnInfo(name="pack_quantity")
    @SerializedName("pack_quantity")
    var packQuantity: Int? = 0,

    @ColumnInfo(name="pack_shop_item_id")
    @SerializedName("pack_shop_item_id")
    var packShopItemId: Int? = null,

    @ColumnInfo(name="deleted_at")
    @SerializedName("deleted_at")
    var deletedAt: Timestamp? = null
)
