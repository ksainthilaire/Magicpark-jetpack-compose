package com.magicpark.data.api

import com.magicpark.data.model.request.authentification.LoginRequest
import com.magicpark.data.model.request.authentification.UpdateUserRequest
import com.magicpark.data.model.request.order.CreateOrderRequest
import io.reactivex.rxjava3.core.Completable
import retrofit2.http.*
import com.magicpark.data.model.request.*
import com.magicpark.data.model.response.*
import kotlinx.coroutines.flow.Flow


interface MagicparkApi {

    @GET("/api/v1/customer/shop")
    fun getShopItems(): Flow<ShopItemsResponse>

    @POST("/api/v1/customer/login")
    fun login(@Body login: LoginRequest): Flow<UserResponse>

    @GET("/api/v1/customer/user")
    fun getUser(): Flow<UserResponse>

    @DELETE("/api/v1/customer/user")
    fun deleteUser(): Completable

    @PUT("/api/v1/customer/user")
    fun updateUser(@Body body: UpdateUserRequest): Completable

    @POST("/api/v1/customer/order")
    fun createOrder(@Body body: CreateOrderRequest): Flow<OrderResponse>

    @GET("/api/v1/customer/order/{id}")
    fun getOrder(@Path("id") ticket: String): Flow<OrderResponse>

    @GET("/api/v1/customer/wallet")
    fun getWallet(): Flow<WalletResponse>

    @GET("/api/v1/customer/shop/categories")
    fun getShopCategories(): Flow<ShopItemResponse>

    @POST("/api/v1/customer/control/{ticket}")
    fun controlTicket(@Path("ticket") ticket: String): Completable

    @GET("/api/v1/customer/settings.json")
    fun getSettings(): Flow<SettingsResponse>

    @POST("/api/v1/customer/help")
    fun help(@Body body: HelpRequest): Completable

}
