package com.magicpark.data.api

import com.magicpark.data.model.request.authentification.LoginRequest
import com.magicpark.data.model.request.authentification.UpdateUserRequest
import com.magicpark.data.model.request.order.CreateOrderRequest
import io.reactivex.rxjava3.core.Completable
import retrofit2.http.*
import com.magicpark.data.model.request.*
import com.magicpark.data.model.response.*

interface MagicparkApi {

    @GET("/api/v1/customer/shop")
    suspend fun getShopItems(): ShopItemsResponse

    @POST("/api/v1/customer/login")
    suspend fun login(@Body login: LoginRequest): UserResponse

    @GET("/api/v1/customer/user")
    suspend fun getUser(): UserResponse

    @DELETE("/api/v1/customer/user")
    fun deleteUser(): Completable

    @PUT("/api/v1/customer/user")
    fun updateUser(@Body body: UpdateUserRequest): Completable

    @POST("/api/v1/customer/order")
    suspend fun createOrder(@Body body: CreateOrderRequest): OrderResponse

    @GET("/api/v1/customer/order/{id}")
    suspend fun getOrder(@Path("id") orderId: String): OrderResponse

    @GET("/api/v1/customer/order/payment/{id}")
    suspend fun getPayment(@Path("id") orderId: String): PaymentResponse

    @GET("/api/v1/customer/order/invoice/{id}")
    suspend fun getInvoice(@Path("id") orderId: String): InvoiceResponse

    @GET("/api/v1/customer/wallet")
    suspend fun getWallet(): WalletResponse

    @GET("/api/v1/customer/shop/categories")
    suspend fun getShopCategories(): ShopItemResponse

    @POST("/api/v1/customer/control/{ticket}")
    fun controlTicket(@Path("ticket") ticket: String): Completable

    @GET("/api/v1/customer/settings.json")
    suspend fun getSettings(): SettingsResponse

    @POST("/api/v1/customer/help")
    fun help(@Body body: HelpRequest): Completable

}
