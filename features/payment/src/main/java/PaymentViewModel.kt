package com.magicpark.features.payment

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class PaymentViewModel : ViewModel() {

    private val _state: MutableStateFlow<PaymentUiState> = MutableStateFlow(PaymentUiState.Loading)
    val state: StateFlow<PaymentUiState> = _state

}
