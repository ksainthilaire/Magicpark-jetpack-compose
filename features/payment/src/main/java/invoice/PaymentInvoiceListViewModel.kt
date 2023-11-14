package com.magicpark.features.payment.invoice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magicpark.domain.model.Invoice
import com.magicpark.domain.usecases.OrderUseCases
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.koin.java.KoinJavaComponent


sealed interface PaymentInvoiceListState {
    /**
     * Initial state
     */
    object Loading : PaymentInvoiceListState

    /**
     * List of user invoices, which have been fetch from the server
     * @param list List of user invoices
     */
    data class InvoiceList(val list: List<Invoice>) : PaymentInvoiceListState
}

class PaymentInvoiceListViewModel : ViewModel() {

    private val orderUseCases: OrderUseCases by KoinJavaComponent.inject(OrderUseCases::class.java)

    val state: StateFlow<PaymentInvoiceListState> =
        flow { emit(orderUseCases.getPaymentInvoices()) }
            .map { invoices -> invoices.toState() }
            .stateIn(viewModelScope, SharingStarted.Eagerly, PaymentInvoiceListState.Loading)

    private fun List<Invoice>.toState() : PaymentInvoiceListState =
        PaymentInvoiceListState.InvoiceList(
            list = this
        )

}
