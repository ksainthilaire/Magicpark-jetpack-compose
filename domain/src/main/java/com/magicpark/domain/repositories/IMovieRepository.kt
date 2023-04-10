package com.magicpark.domain.repositories

import com.magicpark.domain.model.MediaTypeEnum
import com.magicpark.domain.model.Movie
import com.magicpark.domain.model.TimeWindowEnum
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Query

interface IMovieRepository {

    fun auth(apiKey: String): Completable

    fun getMovieDetails(
        id: Int,
        language: String? = null,
        appendToResponse: String? = null
    ): Flowable<Movie>

    fun getTrending(mediaType: MediaTypeEnum, timeWindow: TimeWindowEnum): Flowable<List<Movie>>
    fun getMovieTopRated(language: String, page: Int, region: String): Flowable<List<Movie>>
}