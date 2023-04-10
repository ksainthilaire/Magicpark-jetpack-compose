package io.dvlt.data.api

import io.dvlt.data.http.request.NewSessionRequest
import io.dvlt.data.http.response.SessionResponse
import io.dvlt.data.http.response.TokenResponse
import io.dvlt.data.http.response.TopRatedMoviesResponse
import io.dvlt.data.http.response.TrendingMoviesResponse
import io.dvlt.domain.model.MediaTypeEnum
import io.dvlt.domain.model.TimeWindowEnum
import io.dvlt.data.http.response.MovieDetailsResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

interface MovieDbApi {

    /*
      Create a temporary request token that can be used to validate a TMDB user login.
    */
    @GET("authentication/token/new")
    fun newToken(@Query("api_key") apiKey: String): Observable<TokenResponse>

    /*
      Create a fully valid session ID once a user has validated the request token.
    */
    @POST("authentication/session/new")
    fun createSession(
        @Query("api_key") apiKey: String,
        @Body body: NewSessionRequest
    ): Observable<SessionResponse>

    /*
      Get the daily or weekly trending items.
      The daily trending list tracks items over the period of a day while items have a 24 hour half life.
      The weekly list tracks items over a 7 day period, with a 7 day half life.
    */
    @GET("trending/{media_type}/{time_window}")
    fun getTrending(
        @Path("media_type") mediaType: String,
        @Path("time_window") timeWindow: String,
        @Query("api_key") apiKey: String
    ): Observable<TrendingMoviesResponse>

    /*
      Get the top rated movies on TMDB.
    */
    @GET("movie/top_rated")
    fun getMovieTopRated(
        @Query("api_key") apiKey: String,
        @Query("language") language: String? = null,
        @Query("page") page: Int? = null,
        @Query("region") region: String? = null
    ): Observable<TopRatedMoviesResponse>

    /*
      Get the primary information about a movie.
    */
    @GET("movie/{movie_id}")
    fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String? = null,
        @Query("append_to_response") appendToResponse: String? = null,
    ): Observable<MovieDetailsResponse>

}