package com.magicpark.features.payment.invoice

import androidx.lifecycle.ViewModel
import com.magicpark.domain.model.Invoice
import com.magicpark.domain.usecases.OrderUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    companion object {
        private val TAG = PaymentInvoiceListViewModel::class.java.simpleName
        private const val KEY_ORDER_ID = "KEY_ORDER_ID"
    }

    private val _state: MutableStateFlow<PaymentInvoiceListState> = MutableStateFlow(PaymentInvoiceListState.Loading)
    val state: StateFlow<PaymentInvoiceListState> = _state

    private val orderUseCases: OrderUseCases by KoinJavaComponent.inject(OrderUseCases::class.java)

}
