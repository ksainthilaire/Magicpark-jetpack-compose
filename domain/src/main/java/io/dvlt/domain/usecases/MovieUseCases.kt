package io.dvlt.domain.usecases

import io.dvlt.domain.model.MediaTypeEnum
import io.dvlt.domain.model.Movie
import io.dvlt.domain.model.TimeWindowEnum
import io.dvlt.domain.repositories.IMovieRepository
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