package com.magicpark.domain.model.magicpark

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.sql.Date
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("number")
    var number: String? = null,

    @SerializedName("total_amount")
    var totalAmount: Int? = null,

    @SerializedName("payment_method")
    var paymentMethod: String? = null,

    @SerializedName("payed_at")
    var payedAt: Date? = null
) : Parcelable