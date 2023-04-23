package com.magicpark.features.payment

import android.net.http.SslError
import android.os.Build
import android.util.Log
import android.webkit.*

interface PaymentWebViewListener {
    fun callback(status: PaymentSuccess)
}

enum class PaymentSuccess {
    SUCCESS,
    ERROR,
    CANCEL,
    UNKNOWN
}

class PaymentWebViewClient(
    private val errorUrl: String,
    private val successUrl: String,
    private val cancelUrl: String,
    val listener: PaymentWebViewListener
) : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        return when {
            url.contains(errorUrl) -> {
                listener.callback(PaymentSuccess.ERROR)
                true
            }
            url.contains(cancelUrl) -> {
                listener.callback(PaymentSuccess.CANCEL)
                true
            }
            url.contains(successUrl) -> {
                listener.callback(PaymentSuccess.SUCCESS)
                true
            }
            else -> {
                Log.w("WebView", "don't follow this url : " + url)
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
            listener.callback(PaymentSuccess.UNKNOWN)
        } else {
            listener.callback(PaymentSuccess.UNKNOWN)
        }
    }

    override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
        handler.cancel()
    }
}