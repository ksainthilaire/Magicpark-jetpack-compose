package com.magicpark.features.payment.payment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import com.magicpark.core.MagicparkMaterialTheme
import com.magicpark.utils.ui.CallbackWithoutParameter
import dagger.hilt.android.AndroidEntryPoint
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

@AndroidEntryPoint
class PaymentFragment : Fragment() {

    private val viewModel: PaymentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext())
            .apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    val state by viewModel.state.collectAsState()

                    MagicparkMaterialTheme {
                        PaymentScreen(
                            state = state,
                            onSuccess = viewModel::onSuccess,
                            onFailed = viewModel::onFailed,
                            onCanceled = viewModel::onCanceled
                        ) {
                        }
                    }
                }
            }
}

@Preview
@Composable
private fun PaymentScreenPreview() {
    PaymentScreen(
        state = PaymentState.Payment(
            paymentUrl = "/payment",
            successUrl = "/success",
            errorUrl = "/error",
            cancelUrl = "/cancel",
            200L,
        ),
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
private fun PaymentScreen(
    state: PaymentState,

    onSuccess: CallbackWithoutParameter,
    onFailed: CallbackWithoutParameter,
    onCanceled: CallbackWithoutParameter,
    onBackPressed: CallbackWithoutParameter,
) {

    val paymentListener = rememberSaveable {
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

    when (state) {

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
                        successUrl = state.successUrl,
                        errorUrl = state.errorUrl,
                        cancelUrl = state.cancelUrl,
                        listener = paymentListener
                    )

                    settings.javaScriptEnabled = true
                    settings.loadWithOverviewMode = true
                    settings.useWideViewPort = true
                    settings.setSupportZoom(true)
                    loadUrl(state.paymentUrl)
                }
            }, update = {
                it.loadUrl(state.paymentUrl)
            }
        )
        else -> {}
    }
}
