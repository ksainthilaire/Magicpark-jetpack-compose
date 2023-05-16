package com.magicpark.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class Settings(
    @PrimaryKey var id: Long? = null,

    @ColumnInfo(name="key")
    val key: String? = null,
    @ColumnInfo(name="value")
    val value: String? = null,
)