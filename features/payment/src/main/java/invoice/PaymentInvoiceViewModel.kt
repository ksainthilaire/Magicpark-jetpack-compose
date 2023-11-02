package com.magicpark.features.payment.invoice

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magicpark.domain.model.Invoice
import com.magicpark.domain.usecases.OrderUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.koin.java.KoinJavaComponent

/**
 *
 */
sealed interface  PaymentInvoiceState {
    /**
     * Initial state
     */
    object Loading : PaymentInvoiceState

    /**
     * @param paymentMethod Method of payment
     * @param description Invoice detail
     * @param amount Total invoice amount
     * @param voucher Promotion code
     * @param date Invoice creation date
     */
    data class Invoice(
        val paymentMethod: String?,
        val description: String?,
        val amount: String?,
        val voucher: String?,
        val date: String?,

    ) : PaymentInvoiceState
}

class PaymentInvoiceViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    companion object {
        private val TAG = PaymentInvoiceViewModel::class.java.simpleName
        private const val KEY_ORDER_ID = "KEY_ORDER_ID"
    }

    private val _state: MutableStateFlow<PaymentInvoiceState> = MutableStateFlow(PaymentInvoiceState.Loading)
    val state: StateFlow<PaymentInvoiceState> = _state

    private val orderUseCases: OrderUseCases by KoinJavaComponent.inject(OrderUseCases::class.java)

    private val orderId: Long
        get() = savedStateHandle.get<Long>(KEY_ORDER_ID) ?: 0L

    val invoice: StateFlow<PaymentInvoiceState> =
        flow { emit(orderUseCases.getPaymentInvoice(orderId)) }
            .map { invoice -> invoice.toState() }
            .stateIn(viewModelScope, SharingStarted.Eagerly, PaymentInvoiceState.Loading)

    private fun Invoice.toState() : PaymentInvoiceState.Invoice =
        PaymentInvoiceState.Invoice(
            paymentMethod = paymentMethod,
            description = description,
            amount = amount,
            voucher = voucher,
            date = date
        )
}
