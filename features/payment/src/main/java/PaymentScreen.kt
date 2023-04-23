package com.magicpark.features.payment

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import java.util.*


@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PaymentScreen(navController: NavController? = null, url: String) {

    val paymentListener = rememberSaveable {
        object : PaymentWebViewListener {
            override fun callback(status: PaymentSuccess) {
                when (status) {
                    PaymentSuccess.SUCCESS -> navController?.navigate("/payment/success")
                    PaymentSuccess.UNKNOWN -> navController?.popBackStack()
                    PaymentSuccess.ERROR -> navController?.navigate("/payment/error")
                    PaymentSuccess.CANCEL -> navController?.navigate("/payment/cancel")
                }
            }

        }
    }

    AndroidView(
        modifier = Modifier.padding(top = 100.dp),
        factory = {
            WebView(it).apply {

                setInitialScale(1)

                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = PaymentWebViewClient(
                    successUrl = it.getString(com.magicpark.utils.R.string.payment_success_url),
                    errorUrl = it.getString(com.magicpark.utils.R.string.payment_error_url),
                    cancelUrl = it.getString(com.magicpark.utils.R.string.payment_cancel_url),
                    listener = paymentListener
                )

                settings.javaScriptEnabled = true
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.setSupportZoom(true)
                loadUrl(url)
            }
        }, update = {
            it.loadUrl(url)
        })
}
