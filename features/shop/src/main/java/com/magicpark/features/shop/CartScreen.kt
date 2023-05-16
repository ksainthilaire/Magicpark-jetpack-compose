package com.magicpark.features.shop

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.magicpark.core.MagicparkTheme
import com.magicpark.domain.model.ShopItem
import com.magicpark.features.payment.PaymentMethodDialog
import com.magicpark.features.payment.PaymentMethodDialogListener
import com.magicpark.utils.R
import com.magicpark.utils.ui.Counter
import kotlinx.coroutines.delay

interface CartListener {
    fun updateItem(shopItem: ShopItem)
    fun getItems(): List<ShopItem>
}


private val test_shopItem = ShopItem(
    id = 0,
    name = "First object",
    description = "Description",
    price = 500f,
    imageUrl = "https://img.cuisineaz.com/660x660/2017/02/02/i119528-banane-sauce-chocolat.jpeg"
)

private val test_shopItems: List<ShopItem> = listOf(
    ShopItem(
        id = 0,
        name = "Visite entrée à la journée",
        description = "Un ticket pour une visite et une entrée à la journée",
        imageUrl = "https://i0.wp.com/artistes-productions.com/wp-content/uploads/2020/03/pexels-photo-2014775.jpeg?resize=800%2C533&ssl=1",
        backgroundColor = "",
        price = 5.4f,
        quantity = 1,
        isPack = true,
        packQuantity = 1,
        packShopItemId = 1), ShopItem(
        id = 1,
        name = "Visite entrée au week-end",
        description = "Un ticket pour une visite et une entrée au week-end",
        imageUrl = "https://ca-times.brightspotcdn.com/dims4/default/7249d3d/2147483647/strip/false/crop/4000x2666+0+0/resize/1486x990!/quality/80/?url=https%3A%2F%2Fcalifornia-times-brightspot.s3.amazonaws.com%2Fe2%2Fdb%2F137a883b4ef48700d355f407fe2a%2Fla-et-ticketmaster-taylor-swift.jpg",
        backgroundColor = "",
        price = 5.4f,
        quantity = 1,
        isPack = true,
        packQuantity = 1,
        packShopItemId = 1), ShopItem(
        id = 2,
        name = "Visite entrée au week-end",
        description = "Un ticket pour une visite et une entrée au week-end",
        imageUrl = "https://img.freepik.com/premium-photo/background-paris_219717-5461.jpg",
        backgroundColor = "",
        price = 5.4f,
        quantity = 1,
        isPack = true,
        packQuantity = 1,
        packShopItemId = 1), ShopItem(
        id = 3,
        name = "Visite entrée au mois",
        description = "Un ticket pour une visite et une entrée au week-end",
        imageUrl = "https://decouvrirlemonde.fr/wp-content/uploads/2019/03/monuments-rome-Colise%CC%81e-italie-histoire-empire-romain.jpg",
        backgroundColor = "",
        price = 5.4f,
        quantity = 1,
        isPack = true,
        packQuantity = 1,
        packShopItemId = 1), ShopItem(
        id = 3,
        name = "Visite entrée à l'année",
        description = "Un ticket pour une visite et une entrée à l'année",
        imageUrl = "https://www.lenouvelliste.ch/media/image/94/nf_normal_16_9/chillon-2019.jpg",
        backgroundColor = "",
        price = 5.4f,
        quantity = 1,
        isPack = true,
        packQuantity = 1,
        packShopItemId = 1
    )
)


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
@Preview
fun CartItem(
    shopItem: ShopItem? = test_shopItem, listener: CartListener? = null
) {


    Column(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(30.dp)
    ) {

        Row(Modifier.padding(bottom = 15.dp)) {

            GlideImage(
                model = shopItem?.imageUrl ?: "",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(140.dp)
                    .clip(RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp)),
                contentDescription = ""
            )

            Column(Modifier.padding(start = 20.dp)) {

                Row {
                    Text(
                        text = shopItem?.name ?: "",
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
                    text = shopItem?.description ?: "",
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
                                    text = "%s GNF".format(shopItem?.price.toString()),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun CartScreen(navController: NavController? = null) {


    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.swipe))


    var promoCode by remember { mutableStateOf("") }
    val items = remember { mutableStateOf(listOf<ShopItem>()) }
    var animationVisible by remember { mutableStateOf(true) }
    var paymentMethodDialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = animationVisible) {
        delay(3000L)
        animationVisible = false
    }

    if (paymentMethodDialog) {
        PaymentMethodDialog(object : PaymentMethodDialogListener {
            override fun onClose() {
                paymentMethodDialog = false
            }

            override fun onSelectedPaymentMethod() {
                paymentMethodDialog = false
            }

        })
    }

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



                    Column(Modifier.padding(top=50.dp).constrainAs(items) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }) {

                        test_shopItems.forEach {
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
                                Text("365 USD", Modifier.weight(1f))
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