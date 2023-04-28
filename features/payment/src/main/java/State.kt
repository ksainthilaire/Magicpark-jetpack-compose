package com.magicpark.features.payment

import com.magicpark.domain.model.Movie


sealed class PaymentState {


    data class PaymentUrl(val url: String) : PaymentState()

}