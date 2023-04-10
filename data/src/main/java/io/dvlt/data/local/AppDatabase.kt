package io.dvlt.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import io.dvlt.data.model.MovieEntity


@Database(entities = [MovieEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}