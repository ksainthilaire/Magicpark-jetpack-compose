package com.magicpark.features.payment.payment

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magicpark.domain.enums.PaymentMethod
import com.magicpark.domain.usecases.OrderUseCases
import com.magicpark.utils.R
import com.magicpark.utils.ui.Cart
import com.magicpark.utils.ui.CartState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent

sealed interface PaymentEvent {
    /**
     * The payment failed.
     * @param reason for canceling payment
     */
    data class PaymentFailed(val reason: String) : PaymentEvent

    /**
     * The payment was successful.
     */
    object PaymentSucceeded : PaymentEvent
}

sealed interface  PaymentState {
    /**
     * Initial state.
     */
    object Loading : PaymentState

    /**
     * Payment is in progress
     * @param paymentUrl Payment URL
     *
     * @param successUrl Url to which the user is redirected if successful
     * @param errorUrl Url to which the user is redirected in case of error
     * @param cancelUrl Url to which the user is redirected in case of cancellation
     *
     * @param timeout Time limit from which the payment must be canceled, expressed in milliseconds.
     */
    data class Payment(
        val paymentUrl: String,

        val successUrl: String,
        val errorUrl: String,
        val cancelUrl: String,

        val timeout: Long,
    ) : PaymentState
}

class PaymentViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    companion object {
        private val TAG = PaymentViewModel::class.java.simpleName
        private const val KEY_PAYMENT_METHOD = "KEY_PAYMENT_METHOD_ARG"
        private const val KEY_VOUCHER_CODE = "KEY_VOUCHER_ARG"

        private const val PAYMENT_DEFAUL_TIMEOUT = 3600L
    }

    private val _state: MutableStateFlow<PaymentState> = MutableStateFlow(PaymentState.Loading)
    val state: StateFlow<PaymentState> = _state

    private val _events: MutableSharedFlow<PaymentEvent> = MutableSharedFlow()
    val events: SharedFlow<PaymentEvent> = _events



    private val resources: Resources by KoinJavaComponent
        .inject(Resources::class.java)



    private val cart: Cart by KoinJavaComponent
        .inject(Cart::class.java)

    private val orderUseCases: OrderUseCases by KoinJavaComponent.inject(OrderUseCases::class.java)

    private val paymentMethod: PaymentMethod
        get() = savedStateHandle.get<PaymentMethod>(KEY_PAYMENT_METHOD) ?: PaymentMethod.Orange

    private val voucherCode: String
        get() = savedStateHandle.get<String>(KEY_VOUCHER_CODE) ?: ""

    init {
        startPayment()
    }

    /**
     * Obtain the payment URL for the order created with shopItems, and the discount applied with
     * [voucherCode].
     */
    private fun startPayment() = viewModelScope.launch {
        try {

            val cartState = cart.state.value

            if (cartState !is CartState.Cart) {
                return@launch
            }


            val order = orderUseCases.createOrder(
                shopItems = cartState.items,
                paymentMethod = paymentMethod,
                voucherCode = voucherCode,
            )

            val orderId = order.id
                ?: throw Exception("No order ID was provided after the order was created.")

            val payment = orderUseCases
                .getPayment(orderId)

            val paymentUrl = payment.paymentUrl
                ?: throw Exception("No payment URL is provided for the order. orderId = $orderId.")

            val successUrl = payment.successUrl
                ?: resources.getString(R.string.payment_success_url)

            val errorUrl = payment.errorUrl
                ?: resources.getString(R.string.payment_error_url)

            val cancelUrl = payment.cancelUrl
                ?: resources.getString(R.string.payment_cancel_url)

            val timeout = payment.timeout ?: PAYMENT_DEFAUL_TIMEOUT

           _state.value = PaymentState.Payment(
               paymentUrl = paymentUrl,
               cancelUrl = cancelUrl,
               successUrl = successUrl,
               errorUrl = errorUrl,
               timeout = timeout,
           )
        } catch (e: Exception) {
            Log.e(TAG, "Order creation failed.", e)
            val errorMessage = resources.getString(R.string.register_error)
            _events.emit(PaymentEvent.PaymentFailed(errorMessage))
        }
    }

    /**
     * Function called when the user's payment has been canceled.
     */
    fun onCanceled() = viewModelScope.launch {
        Log.e(TAG, "User payment is successful.")
        val successMessage = resources.getString(R.string.payment_cancel_details)
        _events.emit(PaymentEvent.PaymentFailed(successMessage))
    }

    /**
     * Function called when the user's payment was successful.
     */
    fun onSuccess() = viewModelScope.launch {
        Log.i(TAG, "User payment is successful.")
        _events.emit(PaymentEvent.PaymentSucceeded)
    }

    /**
     * Function called when the user's payment failed.
     */
    fun onFailed() = viewModelScope.launch {
        Log.e(TAG, "User payment failed")
        val failedMessage = resources.getString(R.string.payment_error)
        _events.emit(PaymentEvent.PaymentFailed(failedMessage))
    }
}
