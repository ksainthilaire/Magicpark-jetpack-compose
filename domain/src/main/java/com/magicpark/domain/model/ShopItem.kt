package com.magicpark.domain.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
import java.text.NumberFormat
import java.util.Currency
import java.util.Date
import java.util.Locale

@Entity(tableName = "shop")
@Parcelize
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
    var quantity: Int? = 0,

    @ColumnInfo(name="is_pack")
    @SerializedName("is_pack")
    var isPack: Boolean? = false,

    @ColumnInfo(name="is_favorite")
    @SerializedName("is_favorite")
    var isFavorite: Boolean? = false,
    
    @ColumnInfo(name="pack_quantity")
    @SerializedName("pack_quantity")
    var packQuantity: Int? = 0,

    @ColumnInfo(name="pack_shop_item_id")
    @SerializedName("pack_shop_item_id")
    var packShopItemId: Int? = null,

    @ColumnInfo(name="deleted_at")
    @SerializedName("deleted_at")
    var deletedAt: Date? = null
) : Parcelable, Serializable

val ShopItem.jsonCategories: List<Long>
    get() =  Gson().fromJson(categories, object : TypeToken<List<Long>>() {}.type)

val ShopItem.currentPrice: Float?
    get() = promotionalPrice ?: price


private val format: NumberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
    .apply {
        currency = Currency.getInstance("GNF")
    }



val ShopItem.displayableBasePrice: String?
    get() = format.format(price ?: 0)

val ShopItem.displayablePrice: String?
    get() = format.format(currentPrice ?: 0)

typealias Shop = Pair<List<ShopItem>, List<ShopCategory>>

