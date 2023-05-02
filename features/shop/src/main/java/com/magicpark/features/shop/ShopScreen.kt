package com.magicpark.features.shop

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        categories = "Visite à la journée",
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
        categories = "Visite au mois",
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
        categories = "Visite à l'année",
        quantity = 1,
        isPack = true,
        packQuantity = 1,
        packShopItemId = 1,
        quantityCart = 1
    )
)


private fun getShopItemsCategories(shopItems: List<ShopItem>): List<String> {
    return shopItems.map { it.categories }
        .distinct().filterNotNull()
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ShopItemCard(shopItem: ShopItem) {

    Column(
        Modifier

            .clip(shape = RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(10.dp)
            .wrapContentSize()
    ) {

        Image(
            painterResource(com.magicpark.core.R.drawable.ic_like),

            modifier = Modifier.size(32.dp),
            contentDescription = ""
        )


        GlideImage(
            model = shopItem.imageUrl ?: "",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(top = 20.dp)
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp))
                .size(100.dp),
            contentDescription = ""
        )

        Column {
            Text(
                text = shopItem?.name ?: "",
                style = TextStyle(
                    fontWeight = FontWeight.Bold
                ),
                color = MagicparkTheme.colors.primary
            )
            Text(
                text = "%s GNF".format(shopItem?.price),
                color = MagicparkTheme.colors.primary
            )
        }

    }

}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
@Preview
fun ShopScreen(navController: NavController? = null) {

    val shopItemsList = test_shopItems
    val shopItems by remember { mutableStateOf(shopItemsList) }

    var searchText by remember { mutableStateOf("") }

    var categories = getShopItemsCategories(shopItems)

    println("categories ".plus(categories.joinToString { "," }))

    BottomNavigation(navController) {

        MagicparkContainer {


            Box(
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(id = com.magicpark.core.R.drawable.background_shop_header),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    contentDescription = "description"
                )
                Image(
                    painter = painterResource(id = com.magicpark.core.R.drawable.illustration_elephant),
                    modifier = Modifier
                        .width(76.dp)
                        .height(93.dp)
                        .align(Alignment.CenterStart),
                    contentDescription = "description"
                )
                Text(
                    text = stringResource(R.string.shop_title),
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
            }


            OutlinedTextField(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                value = searchText,
                onValueChange = { value ->
                    searchText = value
                },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    unfocusedBorderColor = Color.Transparent
                ),
                placeholder = { Text(stringResource(id = R.string.shop_label_search)) }
            )

            LazyRow(Modifier.padding(top = 20.dp)) {

                categories.forEach {
                    item {
                        Button(
                            onClick = {
                                TODO("Modifier")
                            },
                        ) {
                            Text(it)
                        }
                        Spacer(Modifier.size(10.dp))
                    }
                }
            }




            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(shopItems) {
                    ShopItemCard(shopItem = it)
                }

            }


        }


    }
}


