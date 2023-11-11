package com.magicpark.ui.webview

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.magicpark.core.MagicparkTheme
import com.magicpark.core.R

@Composable
fun WebViewScreen(onBackPressed: () -> Unit, url: String) {
    Image(
        painter = painterResource(id = R.drawable.ic_back),
        modifier = Modifier
            .width(100.dp)
            .height(50.dp)
            .padding(
                top = MagicparkTheme.defaultPadding,
                end = MagicparkTheme.defaultPadding
            )
            .clickable {
                       onBackPressed()
            },
        contentDescription = null,
        colorFilter = ColorFilter.tint(MagicparkTheme.colors.primary)
    )

    AndroidView(
        modifier = Modifier.padding(top=100.dp),
        factory = {
        WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewClient = WebViewClient()
            loadUrl(url)
        }
    }, update = {
        it.loadUrl(url)
    })
}
