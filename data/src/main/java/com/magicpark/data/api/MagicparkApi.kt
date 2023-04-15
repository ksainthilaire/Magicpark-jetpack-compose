package com.magicpark.data.api

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*
import com.magicpark.data.model.request.*
import com.magicpark.data.model.response.*


interface MagicparkApi {

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