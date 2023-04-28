package com.magicpark.domain.model

import android.graphics.Color
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*


@Parcelize
@Entity(tableName = "user_ticket")
data class UserTicket(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String?,

    @SerializedName("image")
    var imageUrl: String?,

    @SerializedName("backgroundColor")
    var backgroundColor: String?,


    @SerializedName("description")
    val description: String?,

    val createdAt: Date? = null,

    val expiredAt: Date? = null,

    @SerializedName("token")
    var token: String?,
) : Parcelable

fun UserTicket.getBackgroundColor(): Int = Color.parseColor(this.backgroundColor ?: "#F95738")