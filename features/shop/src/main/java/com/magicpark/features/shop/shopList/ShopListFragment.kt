package com.magicpark.features.shop.shopList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.magicpark.core.MagicparkTheme
import com.magicpark.domain.model.ShopCategory
import com.magicpark.domain.model.ShopItem
import com.magicpark.utils.R
import com.magicpark.utils.ui.CallbackWithParameter
import dagger.hilt.android.AndroidEntryPoint
import org.koin.androidx.viewmodel.ext.android.viewModel

@AndroidEntryPoint
class ShopListFragment : Fragment() {

    private val viewModel: ShopListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext())
            .apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    val state by viewModel.state.collectAsState()

                    ShopListScreen(
                        items = emptyList(),

                        currentCategoryId = 0L,
                        categories = emptyList(),

                        goToCategory = {},
                        goToShopItem = {},
                    )
                }
            }
}

@Preview
@Composable
fun ShopListScreen_Preview() =
    ShopListScreen(
        items = emptyList(),

        currentCategoryId = 0L,
        categories = emptyList(),

        goToCategory = {},
        goToShopItem = {},
    )


/**
 * Shop list screen.
 * @param items List of shop items
 *
 * @param currentCategoryId Current category of the shop.
 * @param categories Shop categories
 *
 * @param goToCategory Go to shop category
 * @param goToShopItem Go to shop item
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopListScreen(
    items: List<ShopItem>,

    currentCategoryId: Long,
    categories: List<ShopCategory>,

    goToCategory: CallbackWithParameter<Long>,
    goToShopItem: CallbackWithParameter<ShopItem>,
) {

    var search by remember { mutableStateOf("") }

    Box(
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = com.magicpark.core.R.drawable.background_shop_header),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentDescription = null,
        )
        Image(
            painter = painterResource(id = com.magicpark.core.R.drawable.illustration_elephant),
            modifier = Modifier
                .width(72.dp)
                .height(96.dp)
                .align(Alignment.CenterStart),
            contentDescription = null,
        )
        Text(
            text = stringResource(R.string.shop_title),
            modifier = Modifier.align(Alignment.Center),
            color = Color.White,
        )
    }

    OutlinedTextField(
        modifier = Modifier
            .padding(top = 12.dp)
            .fillMaxWidth(),
        value = search,
        onValueChange = { value ->
            search = value
        },
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = Color.White,
            unfocusedBorderColor = Color.Transparent
        ),
        placeholder = { Text(stringResource(id = R.string.shop_label_search)) },
    )

    LazyRow(Modifier.padding(top = 24.dp)) {

        item {
            Button(
                onClick = {
                    goToCategory(0L)
                },
            ) {
                Text("Promotions \uD83D\uDD16")
            }
        }

        categories.forEach { category ->
            item {
                Button(
                    modifier = Modifier.padding(top = 12.dp),
                    onClick = {
                        val categoryId = category.id ?: 0L
                        goToCategory(categoryId)
                    },
                ) {
                    Text(category.name.toString())
                }
            }
        }
    }

    items.let { shopItems ->
        LazyVerticalGrid(
            modifier = Modifier.padding(top = 12.dp),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(shopItems) { shopItem ->
                ShopItemCard(shopItem = shopItem, goToShopItem)
            }
        }
    }
}

@Composable
private fun ShopItemCard(
    shopItem: ShopItem,
    goToShopItem: CallbackWithParameter<ShopItem>,
) {
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

        Image(
            painter = painterResource(com.magicpark.core.R.drawable.ic_like),
            modifier = Modifier.size(32.dp),
            contentDescription = null,
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
                .padding(top = 24.dp)
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(16.dp, 16.dp, 16.dp, 16.dp))
                .size(96.dp),
            contentDescription = null
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


