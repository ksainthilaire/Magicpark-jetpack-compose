package com.magicpark.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.magicpark.domain.converters.DateConverter
import com.magicpark.domain.model.ShopItem
import com.magicpark.domain.model.UserTicket


@Database(entities = [ShopItem::class, UserTicket::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shopDao(): ShopDao
    abstract fun ticketDao(): UserTicketDao
}
