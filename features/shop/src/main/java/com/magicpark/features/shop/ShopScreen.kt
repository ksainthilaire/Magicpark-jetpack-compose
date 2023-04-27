package com.magicpark.features.shop

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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
            price = 5.4f,
            quantity = 1,
            isPack = true,
            packQuantity = 1,
            packShopItemId = 1,
            quantityCart = 1
        )
    )
}

private fun getShopItemCategory(shopItems: List<ShopItem>): List<String> {
    val categories = listOf<String>()
    return categories
}


@Composable
fun ShopItemCard(shopItem: ShopItem) {

}

private

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun ShopScreen(navController: NavController? = null) {

    val shopItemsList = generateShopItems()
    val shopItems by remember { mutableStateOf(shopItemsList) }

    var searchText by remember { mutableStateOf("") }

    BottomNavigation(navController) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {


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




            }

        }
    }
}
