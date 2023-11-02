package com.magicpark.features.shop.shopItem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import com.magicpark.domain.model.ShopItem
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

                    ShopItemScreen(
                        shopItem = ShopItem(),
                        addToCart = {},
                        onBackPressed = { activity?.onBackPressedDispatcher?.onBackPressed() }
                    )
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

    //   val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(com.magicpark.core.R.raw.abstract_background))

    /*
    val items by viewModel.shop.collectAsState()


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
*/
}
