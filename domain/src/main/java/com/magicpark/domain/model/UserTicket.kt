package com.magicpark.domain.model

import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.magicpark.domain.converters.DateConverter
import java.io.Serializable
import java.util.Calendar
import java.util.Date

@Entity(tableName = "wallet")
data class UserTicket(
    @PrimaryKey var id: Long? = null,

    @SerializedName("payload")
    var payload: String? = null,


    @ColumnInfo(name="name")
    val name: String? = null,

    @ColumnInfo(name="image_url")
    @SerializedName("image_url")
    var imageUrl: String? = null,

    @ColumnInfo(name="background_color")
    @SerializedName("background_color")
    var backgroundColor: String? = null,

    @ColumnInfo(name="shop_item")
    @SerializedName("shop_item")
    val shopItem: Long? = null,

    @ColumnInfo(name="user_id")
    @SerializedName("user_id")
    val userId: Long? = null,

    @ColumnInfo(name="created_at")
    @SerializedName("created_at")
    val createdAt: Date? = null,

    @ColumnInfo(name="expired_at")
    @SerializedName("expired_at")
    var expiredAt: Date? = null,

    @ColumnInfo(name="token")
    @SerializedName("token")
    val token: String? = null,


    @ColumnInfo(name="deleted_at")
    @SerializedName("deleted_at")
    var deletedAt: Date? = null
) : Serializable

fun UserTicket.isExpired(): Boolean {
    val date: Date = Calendar.getInstance().time
    val expiredAt = expiredAt ?: return false
    return (expiredAt.time < date.time)
}
