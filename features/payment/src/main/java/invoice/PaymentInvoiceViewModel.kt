package com.magicpark.features.payment.invoice

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.magicpark.domain.model.Invoice
import com.magicpark.features.payment.PaymentStatusEnum


class PaymentInvoiceViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    companion object {
        private const val KEY_INVOICE_ARG = "KEY_INVOICE_ARG"
        private const val KEY_PAYMENT_STATUS_ARG = "KEY_PAYMENT_STATUS_ARG"
    }

    val invoice: Invoice
        get() = savedStateHandle.get<Invoice>(KEY_INVOICE_ARG) ?: Invoice()

    val paymentStatus: PaymentStatusEnum
        get() = savedStateHandle.get<PaymentStatusEnum>(KEY_PAYMENT_STATUS_ARG)
            ?: PaymentStatusEnum.UNKNOWN
}
