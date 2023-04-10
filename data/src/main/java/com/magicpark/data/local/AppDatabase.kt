package com.magicpark.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.magicpark.data.model.MovieEntity


@Database(entities = [MovieEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}