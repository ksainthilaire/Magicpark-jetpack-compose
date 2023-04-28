package com.magicpark.features.shop

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.magicpark.core.MagicparkTheme
import com.magicpark.domain.model.magicpark.ShopItem
import com.magicpark.ui.menu.BottomNavigation
import com.magicpark.utils.R


private fun generateShopItems(): List<ShopItem> {
    return listOf(
        ShopItem(
            id = 0,
            name = "Visite entrée à la journée",
            description = "Un ticket pour une visite et une entrée à la journée",
            imageUrl = "",
            backgroundColor = "",
            price = 5.4f,
            quantity = 1,
            isPack = true,
            packQuantity = 1,
            packShopItemId = 1,
            quantityCart = 1
        ),
        ShopItem(
            id = 1,
            name = "Visite entrée au week-end",
            description = "Un ticket pour une visite et une entrée au week-end",
            imageUrl = "",
            backgroundColor = "",
            price = 5.4f,
            quantity = 1,
            isPack = true,
            packQuantity = 1,
            packShopItemId = 1,
            quantityCart = 1
        ),
        ShopItem(
            id = 2,
            name = "Visite entrée au week-end",
            description = "Un ticket pour une visite et une entrée au week-end",
            imageUrl = "",
            backgroundColor = "",
            categories = "fraise",
            price = 5.4f,
            quantity = 1,
            isPack = true,
            packQuantity = 1,
            packShopItemId = 1,
            quantityCart = 1
        ),
        ShopItem(
            id = 3,
            name = "Visite entrée au mois",
            description = "Un ticket pour une visite et une entrée au week-end",
            imageUrl = "",
            backgroundColor = "",
            price = 5.4f,
            categories = "banane",
            quantity = 1,
            isPack = true,
            packQuantity = 1,
            packShopItemId = 1,
            quantityCart = 1
        ),
        ShopItem(
            id = 3,
            name = "Visite entrée à l'année",
            description = "Un ticket pour une visite et une entrée à l'année",
            imageUrl = "",
            backgroundColor = "",
            categories = "test",
            price = 5.4f,
            quantity = 1,
            isPack = true,
            packQuantity = 1,
            packShopItemId = 1,
            quantityCart = 1
        )
    )
}

private fun getShopItemsCategories(shopItems: List<ShopItem>): List<String> {
    return shopItems.map { it.categories }
        .distinct().filterNotNull()
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ShopItemCard(shopItem: ShopItem) {

    Column {

        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = Icons.Default.Delete,
            contentDescription = "",
            tint = MagicparkTheme.colors.primary
        )


        GlideImage(
            model = shopItem.imageUrl ?: "",
            contentScale = ContentScale.Fit,
            modifier = Modifier.clip(RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp)),
            contentDescription = ""
        )

        Column {
            Text(
                text = shopItem?.name ?: "",
                style = TextStyle(
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "%s GNF".format(shopItem?.price)
            )
        }

    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun ShopScreen(navController: NavController? = null) {

    val shopItemsList = generateShopItems()
    val shopItems by remember { mutableStateOf(shopItemsList) }

    var searchText by remember { mutableStateOf("") }

    var categories = getShopItemsCategories(shopItems)

    BottomNavigation(navController) {
        LazyColumn(
            modifier = Modifier
                .background(Color.Gray)
                .fillMaxSize()
                .padding(20.dp)
        ) {


            item {


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
                    columns = GridCells.Adaptive(minSize = 128.dp)
                ) {
                    items(shopItems) {
                        ShopItemCard(shopItem = it)
                    }

                }


            }

        }
    }
}

