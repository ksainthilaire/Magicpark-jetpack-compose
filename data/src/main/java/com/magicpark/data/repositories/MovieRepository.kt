package com.magicpark.data.repositories

import com.magicpark.data.api.MovieDbApi
import com.magicpark.data.http.mapHttpError
import com.magicpark.data.http.request.NewSessionRequest
import com.magicpark.data.local.MovieDao
import com.magicpark.data.model.CategoryEnum
import com.magicpark.data.model.toMovie
import com.magicpark.data.model.toMovieEntity
import com.magicpark.data.session.MovieDbSession
import com.magicpark.domain.model.MediaTypeEnum
import com.magicpark.domain.model.Movie
import com.magicpark.domain.model.TimeWindowEnum
import com.magicpark.domain.repositories.IMovieRepository
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.java.KoinJavaComponent.inject

class MovieRepository : IMovieRepository {

    private val movieDbApi: MovieDbApi by inject(MovieDbApi::class.java)
    private val movieDao: MovieDao by inject(MovieDao::class.java)

    private val movieDbSession: MovieDbSession by inject(MovieDbSession::class.java)

    override fun auth(apiKey: String): Completable {
        return movieDbApi.newToken(apiKey)
            .subscribeOn(Schedulers.io())
            .map(::mapHttpError)
            .flatMapCompletable {
                val token = it.requestToken!!


                movieDbSession.saveToken(it.requestToken)

                return@flatMapCompletable movieDbApi.createSession(
                    token,
                    NewSessionRequest(requestToken = token)
                )
                    .map(::mapHttpError)
                    .flatMapCompletable {
                        Completable.complete()
                    }
            }
    }

    override fun getMovieDetails(
        id: Int,
        language: String?,
        appendToResponse: String?
    ): Flowable<Movie> = Flowable.create({ emitter ->
        val cachedData = getMovieFromDatabase(id)
            .subscribe(emitter::onNext, emitter::onError)

        movieDbApi.getMovieDetails(id, movieDbSession.getToken(), language, appendToResponse)
            .subscribeOn(Schedulers.io())
            .subscribe ({ response ->
                cachedData.dispose()

                getMovieFromDatabase(id)
                    .subscribe(emitter::onNext, emitter::onError)
            }, emitter::onError)

    }, BackpressureStrategy.BUFFER)


    override fun getTrending(
        mediaCategory: MediaTypeEnum,
        timeWindow: TimeWindowEnum
    ): Flowable<List<Movie>> = Flowable.create({ emitter ->

        val cachedData = getMoviesFromDatabase(CategoryEnum.TRENDS)
            .subscribe(emitter::onNext, emitter::onError)

        movieDbApi.getTrending(mediaCategory.value, timeWindow.value, movieDbSession.getToken())
            .subscribeOn(Schedulers.io())
            .map(::mapHttpError)
            .subscribe({ response ->
                cachedData.dispose()
                response.results?.let { saveMovies(it, CategoryEnum.TRENDS) }
                getMoviesFromDatabase(CategoryEnum.TRENDS)
                    .onErrorReturnItem(listOf())
                    .subscribe(emitter::onNext)
            }, emitter::onError)

    }, BackpressureStrategy.BUFFER)


    override fun getMovieTopRated(
        language: String,
        page: Int,
        region: String
    ): Flowable<List<Movie>> = Flowable.create({ emitter ->

        val cachedData = getMoviesFromDatabase(CategoryEnum.TOP_RATED)
            .subscribe(emitter::onNext, emitter::onError)

        movieDbApi.getMovieTopRated(movieDbSession.getToken())
            .subscribeOn(Schedulers.io())
            .map(::mapHttpError)
            .subscribe({ response ->
                cachedData.dispose()

                response.results?.let { saveMovies(it, CategoryEnum.TOP_RATED) }
                getMoviesFromDatabase(CategoryEnum.TOP_RATED)
                    .onErrorReturnItem(listOf())
                    .subscribe(emitter::onNext)
            }, emitter::onError)
    }, BackpressureStrategy.BUFFER)


    private fun getMoviesFromDatabase(Category: CategoryEnum = CategoryEnum.NORMAL): Observable<List<Movie>> =
        movieDao.getMovies(Category)
            .subscribeOn(Schedulers.io())
            .map { movies -> movies.map { it.toMovie() } }
            .onErrorReturnItem(listOf())

    private fun getMovieFromDatabase(id: Int): Observable<Movie> = movieDao.getMovie(id)
        .subscribeOn(Schedulers.io())
        .map { it.toMovie() }
        .onErrorReturnItem(Movie())

    private fun saveMovies(
        movies: List<Movie>,
        category: CategoryEnum = CategoryEnum.NORMAL
    ) = movieDao.saveMovies(movies
        .map { toMovieEntity(it, category) })
}