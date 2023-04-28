package com.magicpark.domain.model.magicpark

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user")
data class User(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,

    @SerializedName("fullName")
    val fullName: String?,

    @SerializedName("country")
    val country: String? = null,

    @SerializedName("mail")
    val mail: String? = null,

    @SerializedName("token")
    val token: String? = null,


    @SerializedName("phone_number")
    val phoneNumber: String? = null,

    @SerializedName("role")
    var role: UserRank = UserRank.CUSTOMER,

    )