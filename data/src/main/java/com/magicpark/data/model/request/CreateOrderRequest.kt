package com.magicpark.data.model.request.order

import com.google.gson.annotations.SerializedName
import com.magicpark.data.model.request.ShopItemRequest
import com.magicpark.domain.enums.PaymentMethod
import com.magicpark.domain.model.ShopItem

data class CreateOrderRequest(
    @SerializedName("items")
    val items: List<ShopItemRequest>? = null,

    @SerializedName("voucher_code")
    val voucherCode: String? = null,

    @SerializedName("payment_method")
    val paymentMethod: PaymentMethod
) 
