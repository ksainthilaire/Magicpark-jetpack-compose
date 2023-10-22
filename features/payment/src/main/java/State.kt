package com.magicpark.features.payment


sealed interface  PaymentUiState {

    
    object Loading : PaymentUiState

    data class PaymentUrl(val url: String) : PaymentUiState

}
