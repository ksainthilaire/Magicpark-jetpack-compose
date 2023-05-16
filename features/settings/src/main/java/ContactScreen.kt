package com.magicpark.features.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.magicpark.core.MagicparkTheme




@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ContactScreen(navController: NavController? = null) {



    Image(
        painter = painterResource(id = com.magicpark.core.R.drawable.ic_back),
        modifier = Modifier
            .width(100.dp)
            .height(50.dp)
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

    var text by remember { mutableStateOf("") }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Column(
                Modifier.padding(start=20.dp, end=20.dp)
            ){
                Text(
                    text = "Pouvez-vous décrire le problème que vous rencontrer?",
                    modifier = Modifier.padding(top = 80.dp),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold
                    )

                )
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = "Nous ferons part de votre commentaire au support Magicpark afin d'améliorer notre service et éviter que ce problème se reproduise."
                )
            }

            OutlinedTextField(
                modifier = Modifier.padding(top=20.dp)
                    .height(400.dp)
                    .width(300.dp),
                value = text,
                onValueChange = { value ->
                    text = value
                },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    unfocusedBorderColor = Color.Transparent
                ),
                placeholder = { Text("Renseignez-nous ici le problème rencontré") }
            )


            Button(
                modifier = Modifier.padding(top=50.dp),
                onClick = {
                    TODO("Modifier")
                },
            ) {
                Text("Transmettre au support")
            }

        }
    }
}