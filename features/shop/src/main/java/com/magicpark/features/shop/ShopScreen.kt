package com.magicpark.features.shop

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.magicpark.core.MagicparkTheme
import com.magicpark.domain.model.ShopItem
import com.magicpark.ui.menu.BottomNavigation
import com.magicpark.utils.R
import com.magicpark.utils.ui.MagicparkContainer
import com.magicpark.utils.ui.OnLifecycleEvent


@Composable
fun ShopItemCard(shopItem: ShopItem, onClick: (shopItem: ShopItem) -> Unit) {

    Column(
        Modifier
            .clickable {
                onClick(shopItem)
            }
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
                .padding(top = 20.dp)
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp))
                .size(100.dp),
            contentDescription = ""
        )

        Column {
            Text(
                text = shopItem.name ?: "",
                style = TextStyle(
                    fontWeight = FontWeight.Bold
                ),
                color = MagicparkTheme.colors.primary
            )

            Text(
                text = "%s GNF".format(shopItem.price),
                style = if (shopItem.promotionalPrice != null) TextStyle(textDecoration = TextDecoration.LineThrough) else
                    TextStyle(),
                color = MagicparkTheme.colors.primary
            )

            if (shopItem.promotionalPrice != null) {
                Text(
                    text = "%s GNF".format(shopItem.promotionalPrice),
                    color = MagicparkTheme.colors.primary
                )
            }
        }

    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun ShopScreen(navController: NavController? = null, viewModel: ShopViewModel) {


    var searchText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(1) }

    val shop by viewModel.shop.collectAsState()

    var shopItems = shop?.first ?: listOf()
    val categories = shop?.second ?: listOf()




    /*
        TODO: Search text
        TODO: Category
     */



    shopItems = shopItems.filter { it.name?.lowercase()?.contains(searchText)  ?: true }

    if (selectedCategory == -1)
        shopItems = shopItems.filter { it.promotionalPrice != null }
    else
        shopItems = shopItems.filter { it.categories?.contains(selectedCategory.toString()) ?: false }

   // shopItems = shopItems.filter { it.categories?.contains(selectedCategory) ?: false }

    println("categories ".plus(categories.joinToString { "," }))


    OnLifecycleEvent { _, event ->

        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                viewModel.loadShopList()
            }
            else -> {}
        }
    }

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

                item {
                    Button(
                        onClick = {
                            selectedCategory = -1
                        },
                    ) {
                        Text("Promotions \uD83D\uDD16")
                    }
                    Spacer(Modifier.size(10.dp))
                }

                categories.forEach {
                    item {
                        Button(
                            onClick = {
                                selectedCategory = it.id?.toInt() ?: 0
                            },
                        ) {
                            Text(it.name.toString())
                        }
                        Spacer(Modifier.size(10.dp))
                    }
                }



            }



            shopItems.let { shopItems ->

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(shopItems) {
                        ShopItemCard(shopItem = it) { shopItem ->
                            navController?.navigate("/shop/${shopItem.id}")
                        }
                    }

                }

            }


        }


    }
}


