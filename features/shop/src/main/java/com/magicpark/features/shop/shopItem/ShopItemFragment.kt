package com.magicpark.features.shop.shopItem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.magicpark.core.MagicparkMaterialTheme
import com.magicpark.core.MagicparkTheme
import com.magicpark.domain.model.ShopItem
import com.magicpark.utils.R
import com.magicpark.utils.ui.CallbackWithParameter
import com.magicpark.utils.ui.CallbackWithoutParameter
import dagger.hilt.android.AndroidEntryPoint
import org.koin.androidx.viewmodel.ext.android.viewModel


@AndroidEntryPoint
class ShopItemFragment : Fragment() {

    private val viewModel: ShopItemViewModel by viewModel()

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
                        ShopItemScreen(
                            shopItem = viewModel.shopItem,
                            addToCart = viewModel::addProduct,
                            onBackPressed = { findNavController().navigate(com.magicpark.features.shop.R.id.cartFragment)}//activity?.onBackPressedDispatcher?.onBackPressed() }
                        )
                    }
                }
            }
}

@Preview
@Composable
fun ShopItemScreen_Preview() =
    ShopItemScreen(
        shopItem = ShopItem(),
        addToCart = {},
        onBackPressed = {}
    )

@Composable
fun ShopItemScreen(
    shopItem: ShopItem,
    addToCart: CallbackWithParameter<ShopItem>,
    onBackPressed: CallbackWithoutParameter,
) {
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
                Text(
                    text = "%s GNF".format(shopItem.price),
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
                        text = "%s GNF".format(shopItem.promotionalPrice),
                        style = TextStyle(
                            textDecoration = TextDecoration.Underline,
                            fontSize = 32.sp
                        ),
                        color = MagicparkTheme.colors.primary
                    )
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
                    addToCart(shopItem)
                    onBackPressed()
                },
            )
            {
                Text(text = stringResource(R.string.cart_button_pay))
            }
        }
    }
}
