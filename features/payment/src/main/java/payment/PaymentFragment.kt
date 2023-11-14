package com.magicpark.features.payment.payment

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.magicpark.utils.ui.CallbackWithoutParameter
import com.magicpark.utils.ui.LoadingScreen
import org.koin.androidx.compose.getViewModel
import java.util.*

@Preview
@Composable
private fun PaymentScreenPreview() {
    PaymentScreen(
        onSuccess = {},
        onFailed = {},
        onCanceled = {},
        onBackPressed = {})
}

/**
 * @param onSuccess listener successful payment
 * @param onFailed listener payment failed
 * @param onCanceled listener payment canceled
 * @param onBackPressed listener go back
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PaymentScreen(
    onSuccess: CallbackWithoutParameter,
    onFailed: CallbackWithoutParameter,
    onCanceled: CallbackWithoutParameter,
    onBackPressed: CallbackWithoutParameter,
) {

    val viewModel: PaymentViewModel = getViewModel()
    val state by viewModel.state.collectAsState()

    val paymentListener = remember {
        object : PaymentWebViewListener {
            override fun setStatus(status: PaymentWebViewState) {
                when (status) {
                    PaymentWebViewState.SUCCESS -> onSuccess()
                    PaymentWebViewState.UNKNOWN -> onBackPressed()
                    PaymentWebViewState.ERROR -> onFailed()
                    PaymentWebViewState.CANCEL -> onCanceled()
                }
            }
        }
    }

    when (val paymentState = state) {

        is PaymentState.Loading ->
            LoadingScreen()

        is PaymentState.Payment ->
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
                            successUrl = paymentState.successUrl,
                            errorUrl = paymentState.errorUrl,
                            cancelUrl = paymentState.cancelUrl,
                            listener = paymentListener
                        )

                        settings.javaScriptEnabled = true
                        settings.loadWithOverviewMode = true
                        settings.useWideViewPort = true
                        settings.setSupportZoom(true)
                        loadUrl(paymentState.paymentUrl)
                    }
                }, update = {
                    it.loadUrl(paymentState.paymentUrl)
                }
            )
    }
}
