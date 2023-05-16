package com.magicpark.data.model.request.order

import com.google.gson.annotations.SerializedName
import com.magicpark.domain.enums.PaymentMethodEnum
import com.magicpark.domain.model.ShopItem

data class CreateOrderRequest(
    @SerializedName("items")
    val items: List<ShopItem>? = null,

    @SerializedName("voucher_code")
    val voucherCode: String? = null,

    @SerializedName("payment_method")
    val paymentMethod: PaymentMethodEnum
) 