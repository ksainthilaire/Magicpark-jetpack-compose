package com.magicpark.ui.splash


import androidx.annotation.StringRes
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.magicpark.app.R
import kotlinx.coroutines.delay


@Composable
fun Title(
    @StringRes id: Int,
    fontSize: TextUnit = 28.sp,
    fontWeight: FontWeight = FontWeight.Bold
) {
    Text(
        text = stringResource(id),
        textAlign = TextAlign.Center,
        style = TextStyle(
            fontSize = fontSize,
            fontWeight = fontWeight,
            color = Color.Red
        )
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
@Preview
fun SplashScreen(onContinue: (() -> Unit)? = null) {


    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.shine))

    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(visible) {
        delay(500L)
        visible = true
    }

    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.Black)
            .padding(top = 20.dp, bottom = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Title(com.magicpark.utils.R.string.splash_title)
        Title(
            com.magicpark.utils.R.string.splash_subtitle,
            fontSize = 26.sp,
            fontWeight = FontWeight.Normal
        )



        Spacer(modifier = Modifier.weight(1f))


        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        ) {

            LottieAnimation(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                composition = composition, iterations = LottieConstants.IterateForever
            )


            Image(
                painterResource(id = R.drawable.illustration_magicpark),
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp),
                contentDescription = null
            )

        }



        Spacer(modifier = Modifier.weight(1f))

        AnimatedVisibility(
            visible = visible,
            enter = scaleIn() + expandVertically(expandFrom = Alignment.CenterVertically),
            exit = scaleOut() + shrinkVertically(shrinkTowards = Alignment.CenterVertically)
        ) {
            Image(
                painterResource(id = R.drawable.background_button_skip),
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .clickable {
                        onContinue?.invoke()
                    },
                contentDescription = null,
            )
        }

    }
}

