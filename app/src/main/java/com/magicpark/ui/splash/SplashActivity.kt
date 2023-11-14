package com.magicpark.ui.splash


import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Announcement
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.magicpark.core.MagicparkMaterialTheme
import com.magicpark.main.MainActivity
import com.magicpark.utils.R
import com.magicpark.utils.ui.CallbackWithoutParameter
import com.magicpark.utils.ui.InternetRequiredDialog
import dagger.hilt.android.AndroidEntryPoint
import org.koin.androidx.viewmodel.ext.android.viewModel

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModel()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            ComposeView(this)
                .apply {
                    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                    setContent {
                        MagicparkMaterialTheme {

                            val state by viewModel.state.collectAsState()

                            SplashScreen(
                                state = state,
                                goToUpdate = {},
                                goToMain = {
                                    val intent = Intent(context, MainActivity::class.java)
                                    startActivity(intent)
                                },
                                tryAgain = { viewModel.fetchRequiredData() },
                            )
                        }
                    }
                }
        )
    }
}

@Preview
@Composable
fun UpdateRequiredDialog(
    update: (() -> Unit)? = null,
) {
    AlertDialog(
        icon = {
            Icon(
                imageVector = Icons.Filled.Announcement,
                contentDescription = null,
            )
        },
        title = {
            Text(
                text = stringResource(id = R.string.update_required_title),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                modifier = Modifier.padding(horizontal = 12.dp),
                text = stringResource(R.string.update_required_text),
                textAlign = TextAlign.Justify
            )
        },
        onDismissRequest = {},
        confirmButton = {
            Button(
                onClick = { update?.invoke() }
            ) { Text(text = stringResource(id = R.string.update_required_maj)) }
        },
        dismissButton = {}
    )
}

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

@Composable
fun SplashScreen(
    state: SplashUiState,

    goToMain: CallbackWithoutParameter,
    goToUpdate: CallbackWithoutParameter,
    tryAgain: CallbackWithoutParameter,
) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.lottie_shine))

    Box(Modifier
        .fillMaxSize()
        .background(Color(0xFFFEBA28))) {

        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(vertical = 32.dp, horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {

            Title(R.string.splash_title)
            Title(
                R.string.splash_subtitle,
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
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                )

                Image(
                    painterResource(id = R.drawable.illustration_magicpark),
                    modifier = Modifier
                        .size(200.dp),
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            AnimatedVisibility(
                visible = state is SplashUiState.Completed,
                enter = scaleIn() + expandVertically(expandFrom = Alignment.CenterVertically),
                exit = scaleOut() + shrinkVertically(shrinkTowards = Alignment.CenterVertically)
            ) {
                Image(
                    painterResource(id = R.drawable.background_button_skip),
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .clickable { goToMain() },
                    contentDescription = null,
                )
            }
        }

        if (state is SplashUiState.ApplicationUpdateRequired) { UpdateRequiredDialog(update = goToUpdate) }

        if (state is SplashUiState.InternetRequired) { InternetRequiredDialog(tryAgain = tryAgain) }
    }
}

