package com.magicpark.features.login.webview

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.magicpark.core.MagicparkMaterialTheme
import com.magicpark.utils.R
import com.magicpark.utils.ui.LoadingScreen
import dagger.hilt.android.AndroidEntryPoint

private enum class WebViewState {
    Loading,
    Completed,
}

@AndroidEntryPoint
class PrivacyPolicyFragment : Fragment() {

    companion object {
        private val TAG = PrivacyPolicyFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext())
            .apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    MagicparkMaterialTheme {
                        WebViewScreen(
                            onBackPressed = { activity?.onBackPressedDispatcher?.onBackPressed() },
                            url = resources.getString(R.string.privacy_policy_url)
                        )
                    }
                }
            }
}

@Composable
fun WebViewScreen(onBackPressed: () -> Unit, url: String) {
    var state by remember { mutableStateOf(WebViewState.Loading) }

    Box(
        Modifier
            .fillMaxSize()) {

        AndroidView(
            factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    webViewClient = object : WebViewClient() {

                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            super.onPageStarted(view, url, favicon)
                            state = WebViewState.Loading
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            state = WebViewState.Completed
                            super.onPageFinished(view, url)
                        }
                    }
                    loadUrl(url)
                }
            }, update = {
                it.loadUrl(url)
            })

        if (state == WebViewState.Loading) {
            LoadingScreen()
        }

        Image(
            painter = painterResource(id = com.magicpark.core.R.drawable.background_ticket_close),
            modifier = Modifier
                .padding(30.dp)
                .width(30.dp)
                .height(30.dp)
                .clickable {
                    onBackPressed()
                },
            contentDescription = null,
        )

    }
}
