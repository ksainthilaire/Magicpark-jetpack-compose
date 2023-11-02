package com.magicpark.domain.model

import com.google.gson.annotations.SerializedName

data class Payment(

    @SerializedName("amount")
    var amount: Float? = null,

    @SerializedName("payment_url")
    var paymentUrl: String? = null,

    @SerializedName("success_url")
    var successUrl: String? = null,

    @SerializedName("error_url")
    var errorUrl: String? = null,

    @SerializedName("cancel_url")
    var cancelUrl: String? = null,

    @SerializedName("timeout")
    var timeout: Long? = null,
)
