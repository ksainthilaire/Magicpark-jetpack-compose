package com.magicpark.features.payment


sealed class PaymentState {


    data class PaymentUrl(val url: String) : PaymentState()

}