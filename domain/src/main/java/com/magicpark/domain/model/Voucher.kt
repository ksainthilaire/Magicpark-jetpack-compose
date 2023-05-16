package com.magicpark.domain.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import java.util.Date
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

@Entity(tableName = "vouchers")
data class Voucher(
    @PrimaryKey var id: Long? = null,

    @ColumnInfo(name = "voucher_code")
    @SerializedName("voucher_code")
    var voucherCode: String? = null,

    @ColumnInfo(name = "amount")
    @SerializedName("amount")
    var amount: Float? = null,

    @ColumnInfo(name = "created_at")
    @SerializedName("created_at")
    var createdAt: Date? = null,

    @ColumnInfo(name = "deleted_at")
    @SerializedName("deleted_at")
    var deletedAt: Date? = null
)