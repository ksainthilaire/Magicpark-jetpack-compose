package com.magicpark.features.shop.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.magicpark.core.MagicparkTheme
import com.magicpark.domain.model.ShopItem
import com.magicpark.utils.ui.Counter
import org.koin.androidx.viewmodel.ext.android.viewModel

class CartFragment : Fragment() {

    private val viewModel: CartViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext())
            .apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    val state by viewModel.state.collectAsState()

                    CartScreen(
                        state = CartState.Loading,

                        addFromCart = { _, _ -> },
                        removeFromCart = {}
                    )
                }
            }
}

@Preview
@Composable
fun CartScreen_Preview() =
    CartScreen(
        state = CartState.Cart(
            items = emptyList(),
            categories = emptyList(),
        ),

        addFromCart = { _, _ -> },
        removeFromCart = {},
    )

/**
 * Cart screen, contains the list of items the user wants to purchase.
 *
 *  @param state
 *  @param addFromCart shopItem, quantity
 *  @param removeFromCart shopItem, quantity
 */
@Composable
fun CartScreen(
    state: CartState,

    addFromCart: (ShopItem, Long) -> Unit,
    removeFromCart: (Long) -> Unit,
) {
        val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.swipe))


        var promoCode by remember { mutableStateOf("") }

        var animationVisible by remember { mutableStateOf(true) }
        var paymentMethodDialog by remember { mutableStateOf(false) }

        LaunchedEffect(key1 = animationVisible) {
            animationVisible = false
        }


    Box {

        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {

            item {
                Box(Modifier.fillMaxSize()) {

                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {

                        val (back, items, swipAnimation, cart) = createRefs()



                        Column(
                            Modifier
                                .padding(top = 50.dp)
                                .constrainAs(items) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                }) {
                            shopItems?.forEach {
                                CartItem(it)
                            }
                        }



                        Row(modifier = Modifier
                            .background(Color.Transparent)
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .constrainAs(cart) {
                                top.linkTo(items.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                            }) {

                            OutlinedTextField(modifier = Modifier
                                .padding(top = 10.dp)
                                .weight(1f),
                                value = promoCode,
                                onValueChange = { value ->
                                    promoCode = value
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    containerColor = Color.White,
                                    unfocusedBorderColor = MagicparkTheme.colors.primary
                                ),
                                placeholder = { Text(stringResource(id = R.string.cart_promo_code)) })

                            Column(
                                Modifier
                                    .weight(1f)
                                    .padding(start = 10.dp)
                            ) {
                                Row {
                                    Text(
                                        stringResource(id = R.string.cart_label_total),
                                        Modifier.weight(1f),
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Text("${total} GNF", Modifier.weight(1f))
                                }
                                Text(
                                    stringResource(id = R.string.cart_prevent), style = TextStyle(
                                        color = Color.Gray
                                    )
                                )



                                Button(
                                    modifier = Modifier.padding(top = 10.dp),
                                    onClick = {
                                        paymentMethodDialog = true
                                    },
                                ) {
                                    Text(text = stringResource(R.string.cart_buy))
                                }

                            }

                        }



                        if (animationVisible) {
                            LottieAnimation(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .constrainAs(swipAnimation) {
                                        top.linkTo(parent.top)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    },
                                composition = composition,
                                iterations = LottieConstants.IterateForever
                            )
                        }



                        Image(
                            painter = painterResource(id = com.magicpark.core.R.drawable.ic_back),
                            modifier = Modifier
                                .width(100.dp)
                                .height(50.dp)
                                .constrainAs(back) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                }
                                .padding(
                                    top = MagicparkTheme.defaultPadding,
                                    end = MagicparkTheme.defaultPadding
                                )
                                .clickable {
                                    navController?.popBackStack()
                                },
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(MagicparkTheme.colors.primary)
                        )

                    }

                }
            }
        }
    }
}

/**
 * @param shopItem
 */
@Composable
private fun CartItem(
    shopItem: ShopItem,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(32.dp)
    ) {

        Row(Modifier.padding(bottom = 16.dp)) {

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
                    .size(140.dp)
                    .clip(RoundedCornerShape(16.dp, 16.dp, 16.dp, 16.dp)),
                contentDescription = ""
            )

            Column(Modifier.padding(start = 24.dp)) {

                Row {
                    Text(
                        text = shopItem.name ?: "",
                        modifier = Modifier.weight(1f),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold
                        ),
                        fontSize = 16.sp
                    )

                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = Icons.Default.Delete,
                        contentDescription = "",
                        tint = MagicparkTheme.colors.primary
                    )
                }
                Text(
                    modifier = Modifier.align(Alignment.Start),
                    text = shopItem.description ?: "",
                    style = TextStyle(
                        color = Color.Gray
                    ),
                    fontSize = 14.sp
                )


                Row(Modifier.padding(top = 16.dp)) {

                    Column(Modifier.weight(1f)) {
                        Text(
                            modifier = Modifier.padding(bottom = 10.dp),
                            text = stringResource(id = com.magicpark.utils.R.string.cart_quantity)
                        )

                        Row {
                            Column(Modifier.weight(1f)) {
                                Counter()
                            }
                            Column(
                                Modifier.weight(0.5f),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "%s GNF".format(shopItem.price.toString()),
                                    modifier = Modifier.align(Alignment.End),
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
        Divider(color = Color.LightGray, thickness = 1.dp)
    }
}
