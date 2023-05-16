package com.magicpark.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.magicpark.domain.model.ShopItem
import com.magicpark.domain.model.UserTicket


@Database(entities = [ShopItem::class, UserTicket::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shopDao(): ShopDao
    abstract fun ticketDao(): UserTicketDao
}