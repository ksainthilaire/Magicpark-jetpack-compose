package com.magicpark.features.shop

import androidx.compose.foundation.background
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.magicpark.domain.model.magicpark.ShopItem
import com.magicpark.utils.ui.Counter

interface CartListener {
    fun updateItem(shopItem: ShopItem)
    fun getItems(): List<ShopItem>
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
@Preview
fun CartItem(
    shopItem: ShopItem? = null,
    listener: CartListener? = null
) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(30.dp)
    ) {

        Row {

            GlideImage(
                model = shopItem?.imageUrl ?: "",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp)),
                contentDescription = ""
            )

            Column {

                Row {
                    Text(
                        text = shopItem?.name ?: "",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold
                        ),
                        fontSize = 30.sp
                    )
                    Icon(
                        modifier = Modifier.size(40.dp),
                        imageVector = Icons.Default.Delete,
                        contentDescription = "",
                        tint = Color.Green
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


                Row {

                    Column {
                        Text(text = stringResource(id = com.magicpark.utils.R.string.cart_quantity))
                        Counter()
                    }

                    Text(
                        text = shopItem?.price.toString() ?: "",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun CartScreen() {
    var promoCode by remember { mutableStateOf("") }
    val items = remember { mutableStateOf(listOf<ShopItem>()) }

    LazyColumn(Modifier.fillMaxSize()) {

        item {

            Column {

            }

            Row {

                OutlinedTextField(
                    modifier = Modifier.padding(top = 10.dp),
                    value = promoCode,
                    onValueChange = { value ->
                        promoCode = value
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    placeholder = { Text(stringResource(id = com.magicpark.utils.R.string.cart_promo_code)) }
                )

                Column {
                    Row {
                        Text(
                            stringResource(id = com.magicpark.utils.R.string.cart_label_total)
                        )
                        Text("365 USD")
                    }
                    Text(
                        stringResource(id = com.magicpark.utils.R.string.cart_prevent)
                    )

                    Button(
                        onClick = {
                            TODO("Payer maintenant")
                        },
                    ) {
                        Text(text = stringResource(com.magicpark.utils.R.string.cart_button_pay))
                    }
                }

            }


        }
    }
}