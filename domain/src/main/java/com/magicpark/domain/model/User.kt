package com.magicpark.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.magicpark.domain.enums.UserRank
import java.util.Date

@Entity(tableName = "users")
data class User(
    @PrimaryKey var id: Long? = null,

    @ColumnInfo(name = "fullname")
    @SerializedName("fullname")
    var fullName: String? = null,


    @ColumnInfo(name = "mail")
    var mail: String? = null,


    @ColumnInfo(name = "country")
    var country: String? = null,

    @ColumnInfo(name = "avatar_url")
    var avatarUrl: String? = null,

    @ColumnInfo(name = "role")
    var role: UserRank = UserRank.CUSTOMER,

    @ColumnInfo(name = "token")
    var token: String? = null,


    @ColumnInfo(name = "ip")
    var ip: String? = null,

    @ColumnInfo(name = "phone_number")
    @SerializedName("phone_number")
    var phoneNumber: String? = null,

    @ColumnInfo(name = "created_at")
    @SerializedName("created_at")
    var createdAt: Date? = Date(),

    @ColumnInfo(name = "updated_at")
    @SerializedName("updated_at")
    var updatedAt: Date? = null,

    @ColumnInfo(name = "deleted_at")
    @SerializedName("deleted_at")
    var deletedAt: Date? = null,
)
