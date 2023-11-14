package com.magicpark.domain.model

import com.google.gson.annotations.SerializedName
import com.magicpark.domain.enums.PaymentMethod

data class Invoice(
    @SerializedName("payment_method")
    var paymentMethod: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("amount")
    var amount: String? = null,

    @SerializedName("voucher")
    var voucher: String? = null,

    @SerializedName("date")
    val date: String? = null,
)

fun Invoice.getAmount(): String {
    return "3"
}

fun Invoice.getPaymentMethod(): String {
    return ""
}

fun Invoice.getDate(): String {
    return "lok"
}
