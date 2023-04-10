package io.dvlt.data.repositories

import io.dvlt.data.api.MovieDbApi
import io.dvlt.data.http.mapHttpError
import io.dvlt.data.http.request.NewSessionRequest
import io.dvlt.data.local.MovieDao
import io.dvlt.data.model.CategoryEnum
import io.dvlt.data.model.toMovie
import io.dvlt.data.model.toMovieEntity
import io.dvlt.data.session.MovieDbSession
import io.dvlt.domain.model.MediaTypeEnum
import io.dvlt.domain.model.Movie
import io.dvlt.domain.model.TimeWindowEnum
import io.dvlt.domain.repositories.IMovieRepository
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