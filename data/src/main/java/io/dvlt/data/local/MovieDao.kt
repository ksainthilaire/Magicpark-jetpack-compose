package io.dvlt.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.dvlt.data.model.CategoryEnum
import io.dvlt.data.model.MovieEntity
import io.reactivex.rxjava3.core.Observable


@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMovies(items: List<MovieEntity>): List<Long>

    @Query("SELECT * FROM movies WHERE category LIKE :category")
    fun getMovies(category: CategoryEnum): Observable<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE id LIKE :id LIMIT 1")
    fun getMovie(id: Int): Observable<MovieEntity>
}
