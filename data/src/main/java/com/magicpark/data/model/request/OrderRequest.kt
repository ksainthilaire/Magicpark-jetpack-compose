package com.magicpark.data.model.request

import com.google.gson.annotations.SerializedName

data class ShopItemSelected(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("quantity")
    var quantity: Int? = null
)

class OrderRequest(
    @SerializedName("items")
    val items: List<ShopItemSelected>,

    @SerializedName("voucher_code")
    val voucherCode: String? = null,

 /*   @SerializedName("payment_method")
    val paymentMethod: PaymentMethodEnum */
) : Request()
