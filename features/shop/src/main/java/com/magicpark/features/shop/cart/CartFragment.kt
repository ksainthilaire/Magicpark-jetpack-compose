package com.magicpark.features.shop.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.magicpark.core.MagicparkTheme
import com.magicpark.domain.enums.PaymentMethod
import com.magicpark.domain.model.ShopItem
import com.magicpark.domain.model.displayablePrice
import com.magicpark.features.payment.payment.PaymentMethodDialog
import com.magicpark.utils.R
import com.magicpark.utils.ui.CallbackWithParameter
import com.magicpark.utils.ui.CallbackWithoutParameter
import com.magicpark.utils.ui.Counter
import org.koin.androidx.compose.getViewModel
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

private val format: NumberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
    .apply {
        currency = Currency.getInstance("GNF")
    }


@Preview
@Composable
fun CartScreen_Preview() =
    CartScreen(
        onBackPressed = {},
        goToPayment = {_, _ -> }
    )

/**
 * Cart screen, contains the list of items the user wants to purchase.
 *
 *  @param goToPayment listener, displays the payment method dialog
 *  @param onBackPressed listener go back
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    goToPayment: (voucher: String, method: PaymentMethod) -> Unit,
    onBackPressed: CallbackWithoutParameter,
) {

    val viewModel: CartViewModel = getViewModel()
    val state by viewModel.state.collectAsState()

    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.lottie_swipe)
    )

    var paymentDialog by remember { mutableStateOf(false) }
    val animation by remember { mutableStateOf(false) }
    var voucher by remember { mutableStateOf("") }

    if (paymentDialog) {
        PaymentMethodDialog(
            onClose = { paymentDialog = false },
            onSelectedPaymentMethod = { paymentMethod ->
                goToPayment(voucher, paymentMethod)
            }
        )
    }

    Box {

        if (state.items.isEmpty()) {
            return@Box CartEmptyScreen(goToShop = onBackPressed)
        }

        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(12.dp)
        )
        {
            item {
                Box(Modifier.fillMaxSize())
                {
                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxSize()
                    )
                    {

                        val (back, items, swipAnimation, cart) = createRefs()
                        Column(
                            Modifier
                                .padding(top = 64.dp)
                                .constrainAs(items) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                }) {

                            state.items.forEach { item ->
                                CartItem(
                                    shopItem = item,
                                    addFromCart = viewModel::addProduct,
                                    removeFromCart = viewModel::removeProduct,
                                    removeAllFromCart = viewModel::removeAllProduct,
                                )
                            }
                        }


                        Row(
                            modifier = Modifier
                                .padding(top = 32.dp)
                                .background(Color.Transparent)
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .constrainAs(cart) {
                                    top.linkTo(items.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    bottom.linkTo(parent.bottom)
                                }
                        ) {
                            OutlinedTextField(modifier = Modifier
                                .padding(top = 12.dp)
                                .weight(1f),
                                value = voucher,
                                onValueChange = { value ->
                                    voucher = value
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    containerColor = Color.White,
                                    unfocusedBorderColor = MagicparkTheme.colors.primary
                                ),
                                placeholder = {
                                    Text(stringResource(id = R.string.cart_promo_code))
                                }
                            )

                            Column(
                                Modifier
                                    .weight(1f)
                                    .padding(start = 12.dp)
                            ) {
                                Row {
                                    Text(
                                        stringResource(id = R.string.cart_label_total),
                                        Modifier.weight(1f),
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Text(text = format.format(state.amount), Modifier.weight(1f))
                                }
                                Text(
                                    stringResource(id = R.string.cart_prevent), style = TextStyle(
                                        color = Color.Gray
                                    )
                                )

                                Button(
                                    modifier = Modifier.padding(top = 10.dp),
                                    onClick = {
                                        paymentDialog = true
                                    },
                                ) {
                                    Text(text = stringResource(R.string.cart_buy))
                                }
                            }
                        }

                        if (animation) {
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
                            painter = painterResource(id = R.drawable.ic_back),
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
                                    onBackPressed()
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
 * Item in user's cart
 * @param @see [ShopItem] @see [ShopItem]
 * @param addFromCart @see [CartScreen]
 * @param removeFromCart @see [CartScreen]
 */
@Composable
private fun CartItem(
    shopItem: ShopItem,

    addFromCart: CallbackWithParameter<ShopItem>,
    removeFromCart: CallbackWithParameter<ShopItem>,
    removeAllFromCart: CallbackWithParameter<ShopItem>,
) {
    Column(
        Modifier
            .clip(RoundedCornerShape(16.dp, 16.dp, 16.dp, 16.dp))
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

            Box {

                Image(
                    painter = painter,
                    modifier = Modifier
                        .size(140.dp)
                        .clip(RoundedCornerShape(16.dp, 16.dp, 16.dp, 16.dp)),
                    contentDescription = null
                )

                Column(
                    Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .background(MagicparkTheme.colors.primary)
                        .padding(4.dp)
                ) {
                    Text(
                        text = "%s".format(shopItem.displayablePrice),
                        style = TextStyle(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

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
                        modifier = Modifier
                            .size(16.dp)
                            .clickable { removeAllFromCart(shopItem) },
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
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
                            modifier = Modifier.padding(bottom = 12.dp),
                            text = stringResource(id = R.string.cart_quantity)
                        )

                        Column {
                            Counter(
                                value = shopItem.quantity ?: 1,
                                addListener = { addFromCart(shopItem) },
                                removeListener = { removeFromCart(shopItem) },
                            )
                        }
                    }
                }
            }
        }
    }
}
