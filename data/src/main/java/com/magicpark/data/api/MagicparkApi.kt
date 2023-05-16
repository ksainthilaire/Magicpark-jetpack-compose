package com.magicpark.data.api

import com.magicpark.data.model.request.authentification.LoginRequest
import com.magicpark.data.model.request.authentification.UpdateUserRequest
import com.magicpark.data.model.request.order.CreateOrderRequest
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*
import com.magicpark.data.model.request.*
import com.magicpark.data.model.response.*
import com.magicpark.domain.model.Order


interface MagicparkApi {

    @GET("/api/v1/customer/shop")
    fun getShopItems(@Header("token") token: String): Observable<ShopItemsResponse>

    @POST("/api/v1/customer/login")
    fun login(@Body login: LoginRequest): Observable<UserResponse>

    @GET("/api/v1/customer/user")
    fun getUser(@Header("token") token: String): Observable<UserResponse>

    @DELETE("/api/v1/customer/user")
    fun deleteUser(@Header("token") token: String): Completable

    @PUT("/api/v1/customer/user")
    fun updateUser(@Header("token") token: String, @Body body: UpdateUserRequest): Completable

    @POST("/api/v1/customer/order")
    fun createOrder(@Header("token") token: String, @Body body: CreateOrderRequest): Observable<OrderResponse>

    @GET("/api/v1/customer/order/{id}")
    fun getOrder(@Header("token") token: String, @Path("id") ticket: String): Observable<OrderResponse>

    @GET("/api/v1/customer/wallet")
    fun getWallet(@Header("token") token: String): Observable<WalletResponse>

    @GET("/api/v1/customer/shop/categories")
    fun getShopCategories(): Observable<ShopItemResponse>

    @POST("/api/v1/customer/control/{ticket}")
    fun controlTicket(@Header("token") token: String, @Path("ticket") ticket: String): Completable

    @GET("/api/v1/customer/settings.json")
    fun getSettings(): Observable<SettingsResponse>

    @POST("/api/v1/customer/help")
    fun help(@Header("token") token: String, @Body body: HelpRequest): Completable

}