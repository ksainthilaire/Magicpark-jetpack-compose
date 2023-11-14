package com.magicpark.features.payment.payment

import android.net.http.SslError
import android.os.Build
import android.util.Log
import android.webkit.*

/**
 * Listener to catch payment status after a user's transaction.
 */
interface PaymentWebViewListener {

    /**
     * Catch payment status
     * @param status @see [PaymentWebViewState]
     */
    fun setStatus(status: PaymentWebViewState)
}

/**
 * The different return states possible during payment.
 */
enum class PaymentWebViewState {
    /**
     * The payment was successful. The user has to pay his bill.
     */
    SUCCESS,

    /**
     *
     * An error occurred during user payment. The error may come from the payment solution,
     * the internet connection, or any I/O operations.
     */
    ERROR,

    /**
     * The user canceled the payment.
     */
    CANCEL,

    /**
     * This state is used only the WebView is not supported by the version of Android that the user.
     */
    UNKNOWN
}

/**
 * Custom WebView for Payment
 * @param errorUrl Redirect url, when an error occurred during payment.
 * @param successUrl Redirect url, when the payment has been made.
 * @param cancelUrl Redirect URL when the payment was canceled.
 * @param listener @see [PaymentWebViewListener]
 */
class PaymentWebViewClient(
    private val errorUrl: String,
    private val successUrl: String,
    private val cancelUrl: String,
    val listener: PaymentWebViewListener,
) : WebViewClient() {

    @Deprecated("Deprecated in Java")
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        return when {
            url.contains(errorUrl) -> {
                listener.setStatus(PaymentWebViewState.ERROR)
                true
            }
            url.contains(cancelUrl) -> {
                listener.setStatus(PaymentWebViewState.CANCEL)
                true
            }
            url.contains(successUrl) -> {
                listener.setStatus(PaymentWebViewState.SUCCESS)
                true
            }
            else -> {
                Log.w("WebView", "don't follow this url : $url")
                false
            }
        }
    }


    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            listener.setStatus(PaymentWebViewState.UNKNOWN)
        } else {
            listener.setStatus(PaymentWebViewState.UNKNOWN)
        }
    }

    override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
        handler.cancel()
    }
}
