package com.magicpark.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop_category")
data class ShopCategory(
    @PrimaryKey var id: Long? = 0L,
    @ColumnInfo(name="name")
    val name: String? = null
)
