package com.magicpark.features.wallet


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.magicpark.core.MagicparkTheme
import com.magicpark.domain.model.magicpark.ShopItem
import com.magicpark.ui.menu.BottomNavigation
import com.magicpark.utils.R
import com.magicpark.utils.ui.MagicparkContainer

import java.util.*



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
        packShopItemId = 1,
        quantityCart = 1
    ), ShopItem(
        id = 1,
        name = "Visite entrée au week-end",
        description = "Un ticket pour une visite et une entrée au week-end",
        imageUrl = "https://ca-times.brightspotcdn.com/dims4/default/7249d3d/2147483647/strip/false/crop/4000x2666+0+0/resize/1486x990!/quality/80/?url=https%3A%2F%2Fcalifornia-times-brightspot.s3.amazonaws.com%2Fe2%2Fdb%2F137a883b4ef48700d355f407fe2a%2Fla-et-ticketmaster-taylor-swift.jpg",
        backgroundColor = "",
        price = 5.4f,
        quantity = 1,
        isPack = true,
        packQuantity = 1,
        packShopItemId = 1,
        quantityCart = 1
    ), ShopItem(
        id = 2,
        name = "Visite entrée au week-end",
        description = "Un ticket pour une visite et une entrée au week-end",
        imageUrl = "https://img.freepik.com/premium-photo/background-paris_219717-5461.jpg",
        backgroundColor = "",
        price = 5.4f,
        quantity = 1,
        isPack = true,
        packQuantity = 1,
        packShopItemId = 1,
        quantityCart = 1
    ), ShopItem(
        id = 3,
        name = "Visite entrée au mois",
        description = "Un ticket pour une visite et une entrée au week-end",
        imageUrl = "https://decouvrirlemonde.fr/wp-content/uploads/2019/03/monuments-rome-Colise%CC%81e-italie-histoire-empire-romain.jpg",
        backgroundColor = "",
        price = 5.4f,
        quantity = 1,
        isPack = true,
        packQuantity = 1,
        packShopItemId = 1,
        quantityCart = 1
    ), ShopItem(
        id = 3,
        name = "Visite entrée à l'année",
        description = "Un ticket pour une visite et une entrée à l'année",
        imageUrl = "https://www.lenouvelliste.ch/media/image/94/nf_normal_16_9/chillon-2019.jpg",
        backgroundColor = "",
        price = 5.4f,
        quantity = 1,
        isPack = true,
        packQuantity = 1,
        packShopItemId = 1,
        quantityCart = 1
    )
)

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
@Preview
fun Ticket(shopItem: ShopItem? = test_shopItems[0]) = Column {

    Column(Modifier.padding(20.dp)) {



        Column(
            Modifier
                .clip(CircleShape.copy(CornerSize(24.dp)))
                .background(Color.White)
                .fillMaxWidth()
        ) {


            Row(Modifier.padding(10.dp)) {
                Column(Modifier.weight(0.7f)) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        GlideImage(
                            model = shopItem?.imageUrl ?: "",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp)),
                            contentDescription = ""
                        )

                        Text(
                            text = shopItem?.name ?: "<name>",
                            modifier = Modifier.padding(start = 10.dp),
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 20.sp
                            )
                        )

                    }

                    Text(
                        text = shopItem?.description ?: "",
                        Modifier.padding(top = 10.dp),
                        style = TextStyle(
                            color = Color.Gray
                        )
                    )

                    Button(
                        modifier = Modifier.padding(top = 10.dp),
                        onClick = {
                            TODO("Forgot")
                        },
                    ) {
                        Text(text = "Valider le ticket")
                    }
                }


                Divider(
                    Modifier
                        .fillMaxHeight()
                        .padding(end = 10.dp)
                        .width(1.dp)
                        .background(Color.Black)
                )


                Column(Modifier.weight(0.3f)) {

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = shopItem?.quantity?.toString() ?: "1",
                            style = TextStyle(
                                color = Color.Black
                            )
                        )

                        Icon(
                            imageVector = ImageVector.vectorResource(com.magicpark.core.R.drawable.ic_ticket),
                            modifier = Modifier
                                .size(24.dp)
                                .padding(start = 10.dp),
                            contentDescription = "drawable icons",
                            tint = Color.Unspecified
                        )
                    }


                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = shopItem?.quantity?.toString() ?: "1",
                            style = TextStyle(
                                color = Color.Black
                            )
                        )

                        Icon(
                            imageVector = Icons.Default.Person,
                            modifier = Modifier
                                .size(24.dp)
                                .padding(start = 10.dp),
                            contentDescription = "drawable icons",
                            tint = Color.Unspecified
                        )


                    }

                    Divider(
                        Modifier
                            .fillMaxWidth()
                            .background(Color.Black)
                            .height(1.dp))

                    Text(
                        text = "Expire le 10-04-2023",
                        Modifier.padding(top = 10.dp),
                        style = TextStyle(
                            color = Color.Gray
                        )
                    )
                }
            }
        }


    }
}



@OptIn(ExperimentalGlideComposeApi::class)
@Composable
@Preview
fun WalletScreen(navController: NavController? = null) {



    BottomNavigation(navController) {
        MagicparkContainer {
            LazyColumn(modifier = Modifier.fillMaxSize()) {

                item {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        TabRow(selectedTabIndex = 0, modifier = Modifier.clip(RoundedCornerShape(topStart=20.dp, topEnd=20.dp))) {
                            Tab(true, {}) {
                                Text(
                                    "À VALIDER",
                                    Modifier.padding(10.dp),
                                    style = TextStyle(fontSize = 24.sp)
                                )
                            }
                            Tab(false, {}) {
                                Text(
                                    "EXPIRÉES",
                                    Modifier.padding(10.dp),
                                    style = TextStyle(fontSize = 24.sp)
                                )
                            }
                        }
                    }

                    test_shopItems.forEach {
                        Ticket(it)
                    }
                }

            }



        }





    }
}