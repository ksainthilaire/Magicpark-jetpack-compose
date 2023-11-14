package com.magicpark.utils.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.magicpark.utils.R

@Composable
@Preview
fun LoadingScreen() {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.lottie_drumsticks))

    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        LottieAnimation(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            composition = composition,
            iterations = LottieConstants.IterateForever
        )
    }
}
