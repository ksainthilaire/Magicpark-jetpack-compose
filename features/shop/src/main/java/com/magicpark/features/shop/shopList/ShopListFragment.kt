package com.magicpark.features.shop.shopList

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
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Size
import com.magicpark.core.MagicparkTheme
import com.magicpark.domain.model.ShopItem
import com.magicpark.domain.model.displayableBasePrice
import com.magicpark.domain.model.displayablePrice
import com.magicpark.utils.R
import com.magicpark.utils.ui.CallbackWithParameter
import org.koin.androidx.compose.getViewModel

@Preview
@Composable
fun ShopListScreen_Preview() =
    ShopListScreen(
        goToShopItem = {},
    )


/**
 * Shop list screen.
 *
 * @param goToShopItem Go to shop item
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopListScreen(
    goToShopItem: CallbackWithParameter<ShopItem>,
) {

    val viewModel: ShopListViewModel = getViewModel()
    val state by viewModel.state.collectAsState()

    var search by remember { mutableStateOf("") }

    Column(
        Modifier
            .padding(horizontal = 24.dp)
            .fillMaxSize()) {

        OutlinedTextField(
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth(),
            value = search,
            singleLine = true,
            onValueChange = { value ->
                search = value
                viewModel.changeSearch(search)
            },
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.White,
                unfocusedBorderColor = Color.Transparent
            ),
            placeholder = { Text(stringResource(id = R.string.shop_label_search)) },
        )

        LazyRow(Modifier.padding(top = 24.dp)) {

            state.categories.forEach { category ->
                item {
                    Button(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        enabled = category.id != state.currentCategory,
                        onClick = {
                            val categoryId = category.id ?: 0L
                            viewModel.changeCategory(categoryId)
                        },
                    ) {
                        Text(category.name.toString())
                    }
                }
            }
        }

        state.items.let { shopItems ->
            LazyVerticalGrid(
                modifier = Modifier.padding(top = 12.dp, bottom = 48.dp),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(shopItems) { shopItem ->
                    ShopItemCard(
                        shopItem = shopItem,
                        goToShopItem = goToShopItem,
                        addFavorite = viewModel::addFavorite,
                        removeFavorite = viewModel::removeFavorite
                    )
                }
            }
        }
    }
}

@Composable
private fun ShopItemCard(
    shopItem: ShopItem,
    goToShopItem: CallbackWithParameter<ShopItem>,
    addFavorite: CallbackWithParameter<ShopItem>,
    removeFavorite: CallbackWithParameter<ShopItem>,
) {

    var isFavorite by remember {
        mutableStateOf(shopItem.isFavorite ?: false)
    }

    Column(
        Modifier
            .clickable {
                goToShopItem(shopItem)
            }
            .clip(shape = RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(10.dp)
            .wrapContentSize()
    ) {

        val drawableRes = if (isFavorite) R.drawable.ic_like
        else R.drawable.ic_no_like

        val favoriteListener = if (isFavorite) removeFavorite else addFavorite

        Image(
            painter = painterResource(drawableRes),
            modifier = Modifier
                .clickable {
                    isFavorite = !isFavorite
                    favoriteListener(shopItem)
                }
                .size(32.dp),
            contentDescription = null,
        )

        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .decoderFactory(SvgDecoder.Factory())
                .data(shopItem.imageUrl)
                .size(Size.ORIGINAL)
                .build(), ImageLoader(LocalContext.current)
        )

        Image(
            painter = painter,
            modifier = Modifier
                .padding(top = 24.dp)
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(16.dp, 16.dp, 16.dp, 16.dp))
                .size(96.dp),
            contentDescription = null,
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
                text = "%s".format(shopItem.displayableBasePrice),
                style = if (shopItem.promotionalPrice != null) TextStyle(textDecoration = TextDecoration.LineThrough) else
                    TextStyle(),
                color = MagicparkTheme.colors.primary
            )

            if (shopItem.promotionalPrice != null) {
                Text(
                    text = "%s".format(shopItem.displayablePrice),
                    color = MagicparkTheme.colors.primary
                )
            }
        }
    }
}


