package com.magicpark.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.magicpark.domain.enums.OrderStatus
import com.magicpark.domain.enums.PaymentMethod
import java.util.Date

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey var id: Long? = null,
    @ColumnInfo(name = "number")
    var number: String? = null,

    @ColumnInfo(name = "total_amount")
    @SerializedName("total_amount")
    var totalAmount: Float? = null,

    @ColumnInfo(name = "payment_method")
    @SerializedName("payment_method")
    var paymentMethod: PaymentMethod? = null,

    @ColumnInfo(name = "user_id")
    @SerializedName("user_id")
    var userId: Long? = null,

    @ColumnInfo(name = "cart")
    var cart: String? = null,

    @ColumnInfo(name = "status")
    var status: OrderStatus? = OrderStatus.PENDING,

    @ColumnInfo(name = "deleted_at")
    @SerializedName("deleted_at")
    var deletedAt: Date? = null,

    @ColumnInfo(name = "validated_at")
    @SerializedName("validated_at")
    var validatedAt: Date? = null,

    @ColumnInfo(name = "token")
    @SerializedName("token")
    var token: String? = null,

    @ColumnInfo(name = "client_secret")
    var clientSecret: String? = null,

    @ColumnInfo(name = "created_at")
    @SerializedName("created_at")
    var createdAt: Date? = null,
)
