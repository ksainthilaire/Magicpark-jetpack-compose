package com.magicpark.features.shop.shopItem

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Size
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.magicpark.core.MagicparkTheme
import com.magicpark.domain.model.ShopItem
import com.magicpark.domain.model.displayableBasePrice
import com.magicpark.domain.model.displayablePrice
import com.magicpark.utils.R
import com.magicpark.utils.ui.CallbackWithoutParameter
import org.koin.androidx.compose.getViewModel

@Preview
@Composable
fun ShopItemScreen_Preview() =
    ShopItemScreen(
        shopItem = ShopItem(),
        onBackPressed = {}
    )

@Composable
fun ShopItemScreen(
    shopItem: ShopItem,
    onBackPressed: CallbackWithoutParameter,
) {
    val viewModel: ShopItemViewModel = getViewModel()

    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.lottie_abstract_background),
    )

    Column(
        Modifier
            .fillMaxSize()
            .background(MagicparkTheme.magicparkBackgroundRed)
    ) {

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
                composition = composition, iterations = LottieConstants.IterateForever,
            )

            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .decoderFactory(SvgDecoder.Factory())
                    .data(shopItem.imageUrl)
                    .size(Size.ORIGINAL)
                    .build(), ImageLoader(LocalContext.current)
            )

            Image(
                painter = painter,
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(200.dp)
                    .height(200.dp),
                contentDescription = null,
            )
        }

        Column(
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp))
                .padding(start = 24.dp, end = 24.dp, top = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Text(shopItem.name ?: "")

                Column {
                    Text(
                        text = "%s".format(shopItem.displayableBasePrice),
                        style = if (shopItem.promotionalPrice != null) {
                            TextStyle(
                                fontSize = 32.sp, textDecoration = TextDecoration.LineThrough
                            )
                        } else {
                            TextStyle(
                                textDecoration = TextDecoration.Underline,
                                fontSize = 32.sp
                            )
                        },
                        color = MagicparkTheme.colors.primary,
                    )

                    if (shopItem.promotionalPrice != null) {
                        Text(
                            text = "%s".format(shopItem.displayablePrice),
                            style = TextStyle(
                                textDecoration = TextDecoration.Underline,
                                fontSize = 32.sp
                            ),
                            color = MagicparkTheme.colors.primary
                        )
                    }
                }
            }

            Text(
                text = shopItem.description ?: "",
                Modifier
                    .padding(top = 24.dp)
                    .align(Alignment.Start),
            )

            Button(
                modifier = Modifier.padding(top = 64.dp),
                onClick = {
                    viewModel.addProduct(shopItem)
                    onBackPressed()
                },
            )
            {
                Text(text = stringResource(R.string.cart_button_pay))
            }
        }
    }
}
