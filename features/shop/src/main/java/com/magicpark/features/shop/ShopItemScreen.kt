package com.magicpark.features.shop

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.magicpark.core.MagicparkTheme
import com.magicpark.utils.R


@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun ShopItemScreen(
    navController: NavController? = null,
    viewModel: ShopViewModel,
    id: Long
) {

    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(com.magicpark.core.R.raw.abstract_background))

    val items by viewModel.shop.observeAsState()


    val shopItem = items?.first?.find { id == it.id }

    Column(
        Modifier
            .fillMaxSize()
            .background(MagicparkTheme.magicparkBackgroundRed)
    ) {


        Image(
            painter = painterResource(id = com.magicpark.core.R.drawable.ic_back),
            modifier = Modifier
                .width(100.dp)
                .height(50.dp)
                .padding(
                    top = MagicparkTheme.defaultPadding,
                    end = MagicparkTheme.defaultPadding
                )
                .clickable {
                    navController?.popBackStack()
                },
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color.White)
        )




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


            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .decoderFactory(SvgDecoder.Factory())
                    .data(shopItem?.imageUrl)
                    .size(Size.ORIGINAL)
                    .build(), ImageLoader(LocalContext.current)
            )

            Image(
                painter = painter,
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(200.dp)
                    .height(200.dp),
                contentDescription = ""
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
                Text(shopItem?.name ?: "")
                Text(
                    text = "%s GNF".format(shopItem?.price),
                    style = if (shopItem?.promotionalPrice != null) TextStyle(
                        fontSize = 32.sp, textDecoration = TextDecoration.LineThrough) else
                        TextStyle(   textDecoration = TextDecoration.Underline,
                            fontSize = 32.sp),
                    color = MagicparkTheme.colors.primary
                )

                if (shopItem?.promotionalPrice != null) {
                    Text(
                        text = "%s GNF".format(shopItem.promotionalPrice),
                        style = TextStyle(   textDecoration = TextDecoration.Underline,
                            fontSize = 32.sp),
                        color = MagicparkTheme.colors.primary
                    )
                }
            }

            Text(
                text = shopItem?.description ?: "",
                Modifier
                    .padding(top = 20.dp)
                    .align(Alignment.Start)
            )


            Button(
                modifier = Modifier.padding(top = 50.dp),
                onClick = {
                    if (shopItem != null) {
                        viewModel.addProduct(shopItem)
                        navController?.popBackStack()
                    }
                },
            ) {
                Text(text = stringResource(R.string.cart_button_pay))
            }

        }

    }

}