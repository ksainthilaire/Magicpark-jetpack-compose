package com.magicpark.ui.webview

import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.magicpark.core.MagicparkTheme
import com.magicpark.core.R
import com.magicpark.utils.ui.LoadingScreen


private enum class WebViewState {
    Loading,
    Completed,
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
            painter = painterResource(id = R.drawable.background_ticket_close),
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
