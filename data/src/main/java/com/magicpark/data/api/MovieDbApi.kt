package com.magicpark.data.api

import com.magicpark.data.http.request.NewSessionRequest
import com.magicpark.data.http.response.SessionResponse
import com.magicpark.data.http.response.TokenResponse
import com.magicpark.data.http.response.TopRatedMoviesResponse
import com.magicpark.data.http.response.TrendingMoviesResponse
import com.magicpark.domain.model.MediaTypeEnum
import com.magicpark.domain.model.TimeWindowEnum
import com.magicpark.data.http.response.MovieDetailsResponse
import io.reactivex.rxjava3.core.Completable
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


    @GET("/api/v1/customer/shop")
    fun getShopItems(@Header("token") token: String): Observable<ShopItemResponse>

    @POST("/api/v1/customer/login")
    fun login(@Body login: LoginRequest): Observable<LoginResponse>

    @GET("/api/v1/customer/user")
    fun getUser(@Header("token") token: String): Observable<UserResponse>

    @GET("/api/v1/customer/deleteUser")
    fun deleteUser(@Header("token") token: String): Completable

    @PUT("/api/v1/customer/user")
    fun updateUser(@Header("token") token: String, @Body body: UpdateUserRequest): Completable

    @POST("/api/v1/customer/order")
    fun createOrder(@Header("token") token: String, @Body body: OrderRequest): Observable<String>

    @GET("/api/v1/customer/wallet")
    fun getWallet(@Header("token") token: String): Observable<WalletResponse>

    @GET("/api/v1/customer/shop/categories")
    fun getShopCategories(): Observable<ShopCategoriesResponse>

    @POST("/api/v1/customer/control/{ticket}")
    fun controlTicket(@Header("token") token: String, @Path("ticket") ticket: String): Completable

    @GET("/api/v1/customer/settings.json")
    fun getSettings(): Observable<SettingsResponse>

}