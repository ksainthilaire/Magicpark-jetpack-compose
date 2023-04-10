package io.dvlt.domain.usecases

import io.dvlt.domain.repositories.IMovieRepository
import io.reactivex.rxjava3.core.Completable
import org.koin.java.KoinJavaComponent.inject

object AuthUseCases {
    private val movieRepository: IMovieRepository by inject(IMovieRepository::class.java)

    fun auth(apiKey: String): Completable {
        return movieRepository.auth(apiKey)
    }
}