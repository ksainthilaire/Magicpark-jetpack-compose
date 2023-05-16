package com.magicpark.domain.model

import androidx.annotation.Size
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.magicpark.data.model.enums.SupportStatusEnum
import java.util.Date
import java.util.*

@Entity(tableName = "support")
data class Support(
    @PrimaryKey var id: Long? = null,
    @Size
    @ColumnInfo(name = "name")
    val name: String? = null,

    @ColumnInfo(name = "user_id")
    @SerializedName("user_id")
    val userId: Long? = null,

    @ColumnInfo(name = "description")
    var description: String? = null,

    @ColumnInfo(name = "content")
    @SerializedName("content")
    var content: String? = null,


    @ColumnInfo(name = "status")
    @SerializedName("status")
    var status: SupportStatusEnum? = SupportStatusEnum.TODO,

    @ColumnInfo(name = "created_at")
    @SerializedName("created_at")
    var createdAt: Date? = Date(),

    @ColumnInfo(name="deleted_at")
    @SerializedName("deleted_at")
    var deletedAt: Date? = null
)
