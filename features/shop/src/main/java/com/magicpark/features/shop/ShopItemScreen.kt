package com.magicpark.features.shop

import android.graphics.Paint.Align
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.magicpark.core.MagicparkTheme
import com.magicpark.ui.menu.BottomNavigation
import com.magicpark.utils.R
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment


@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Preview
@Composable
fun ShopItemScreen() {

    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(com.magicpark.core.R.raw.abstract_background))

    BottomNavigation {




        Column(Modifier.fillMaxSize()           .background(MagicparkTheme.magicparkBackgroundRed)) {
            Box(
                Modifier
                    .background(MagicparkTheme.magicparkBackgroundRed)
                    .weight(1f)
                    .fillMaxWidth(),
            ) {





                LottieAnimation(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .align(Alignment.Center),
                    composition = composition, iterations = LottieConstants.IterateForever
                )

                GlideImage(
                    modifier = Modifier.align(Alignment.Center)
                        .width(200.dp).height(200.dp),
                    model = "https://abbeygardens.ca/wp-content/uploads/2021/08/tickets.png",
                    contentDescription = "article",
                )
            }

            Column(

                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(20.dp))
                    .padding(start = 20.dp, end = 20.dp, top = 50.dp),

                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Ticket avec une entrée")
                    Text(
                        text = "500 000 GNF",
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            textDecoration = TextDecoration.Underline,
                            fontSize = 32.sp
                        )
                    )
                }

                Text(text = "Ce ticket vous permet de visiter le parc pendant une journée entière!",
                    Modifier
                        .padding(top = 20.dp)
                        .align(Alignment.Start))


                    Button(
                        modifier = Modifier.padding(top=50.dp),
                        onClick = {
                            TODO("Achetez le ticket")
                        },
                    ) {
                        Text(text = stringResource(R.string.cart_button_pay))
                    }

            }

        }

    }
}