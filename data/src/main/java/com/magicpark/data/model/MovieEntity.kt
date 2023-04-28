package com.magicpark.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.magicpark.domain.model.Movie
import java.util.Locale.Category


enum class CategoryEnum {
    NORMAL,
    TOP_RATED,
    TRENDS
}

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int? = null,

    @ColumnInfo(name = "title")
    val title: String? = null,

    @ColumnInfo("poster_path")
    val posterPath: String? = null,

    @ColumnInfo(name = "original_title")
    val originalTitle: String? = null,


    @ColumnInfo("release_date")
    val releaseDate: String? = null,


    @ColumnInfo(name = "overview")
    val overview: String? = null,

    @ColumnInfo(name = "vote_average")
    val voteAverage: Int? = null,

    @ColumnInfo(name = "vote_count")
    val voteCount: Int? = null,

    @ColumnInfo(name = "category")
    val category: CategoryEnum = CategoryEnum.NORMAL
)

fun MovieEntity.toMovie(): Movie = Movie(
    id = id,
    title = title,
    posterPath = posterPath,
    originalTitle = originalTitle,
    overview = overview,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    voteCount = voteCount
)

fun toMovieEntity(movie: Movie, category: CategoryEnum): MovieEntity =
    MovieEntity(
        id = movie.id,
        posterPath = movie.posterPath,
        title = movie.title,
        originalTitle = movie.originalTitle,
        releaseDate = movie.releaseDate,
        overview = movie.overview,
        voteAverage = movie.voteAverage?.toInt(),
        voteCount = movie.voteCount?.toInt(),
        category = category
    )

