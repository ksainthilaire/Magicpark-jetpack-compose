package com.magicpark.domain.model.magicpark

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "shop")
data class ShopItem(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("description")
    var description: String? = null,

    @SerializedName("categories")
    var categories: String? = null,

    var imageUrl: String? = null,

    var backgroundColor: String? = null,

    @SerializedName("price")
    var price: Float? = null,

    @Ignore
    @SerializedName("quantity")
    var quantity: Int? = null,

    @SerializedName("is_pack")
    var isPack: Boolean? = false,

    @SerializedName("pack_quantity")
    var packQuantity: Int? = 0,

    @SerializedName("pack_shop_item_id")
    var packShopItemId: Int? = null,

    @Ignore
    var quantityCart: Int = 0
) : Parcelable

fun ShopItem.getCategories() : List<Int>? =
    this.categories?.let { Gson().fromJson(it) }
