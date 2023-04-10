package com.magicpark.domain.usecases

import com.magicpark.domain.model.MediaTypeEnum
import com.magicpark.domain.model.Movie
import com.magicpark.domain.model.TimeWindowEnum
import com.magicpark.domain.repositories.IMovieRepository
import io.reactivex.rxjava3.core.Observable
import org.koin.java.KoinJavaComponent.inject

object MovieUseCases {
    private val movieRepository: IMovieRepository by inject(IMovieRepository::class.java)


    fun getTrending(): Observable<List<Movie>> {
        return movieRepository.getTrending(MediaTypeEnum.MOVIE, TimeWindowEnum.WEEK)
            .toObservable()

    }

    fun getMovieTopRated(): Observable<List<Movie>> {
        return movieRepository.getMovieTopRated("fr", 0, "fr")
            .toObservable()
    }

    fun getMovieDetails(id: Int): Observable<Movie> {
        return movieRepository.getMovieDetails(id)
            .toObservable()
    }
}