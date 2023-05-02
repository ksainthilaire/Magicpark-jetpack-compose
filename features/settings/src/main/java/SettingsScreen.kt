package com.magicpark.features.settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.magicpark.core.MagicparkTheme
import com.magicpark.core.R


@Composable
fun Title(text: String) {

    Text(
        text = text,
        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
        style = TextStyle(
            fontWeight = FontWeight.Bold
        ),
        fontSize = 24.sp
    )
}

@Composable
fun MenuItem(
    @DrawableRes id: Int,
    text: String,
    listener: (() -> Unit)? = null
) {

    Row(
        modifier = Modifier.clickable { listener?.invoke() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Image(
            painterResource(id = id),
            modifier = Modifier
                .width(28.dp)
                .height(28.dp),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MagicparkTheme.colors.primary)
        )

        Text(
            text = text,
            modifier = Modifier.padding(start = 10.dp),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                color = MagicparkTheme.colors.primary
            )
        )
    }
}

@Preview
@Composable
fun SettingsScreen(navController: NavController? = null) {

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->
            // process eith the received image uri
        }


    Image(
        painter = painterResource(id = R.drawable.ic_back),
        modifier = Modifier
            .clickable {  }
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


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 50.dp,
                start = 32.dp,
                end = 32.dp
            )
    ) {

        item {

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Box {

                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .border(2.dp, MagicparkTheme.colors.primary, CircleShape)
                    ) {


                        Image(
                            painter = painterResource(R.drawable.illustration_elephant),
                            modifier = Modifier.fillMaxSize(),
                            contentDescription = "avatar",
                            contentScale = ContentScale.FillBounds
    )
                    }


                    Image(
                        painter = painterResource(R.drawable.ic_upload_picture),
                        contentDescription = "upload avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(start=50.dp)
                            .align(Alignment.BottomEnd)
                            .size(32.dp)
                            .clickable {
                                galleryLauncher.launch("image/*")
                            }
                            .clip(CircleShape)

                    )


                }

                Text(modifier = Modifier.padding(top = 10.dp), text = "SAINT HILAIRE")
                Text(text = "Keny")

            }


            Title("Mon compte")
            MenuItem(R.drawable.ic_edit_user, "Modifier mon profil") {
                navController?.navigate("/account/update")
            }
            MenuItem(R.drawable.ic_support, "Nous contacter") {
                navController?.navigate("/support")
            }


            Title("Légal")
            MenuItem(R.drawable.ic_tos, "Conditions générales") {
                navController?.navigate("/privacy-policy")
            }

            Title("Administration")
            MenuItem(R.drawable.ic_admin_qr, "Contrôler un ticket")
            MenuItem(R.drawable.ic_bo, "Back-office")


            Button(
                onClick = {},
                modifier = Modifier
                    .padding(top = 50.dp)
                    .clickable {
                        TODO("Login with facebook")
                    }
            ) {
                Image(
                    painterResource(R.drawable.ic_logout),
                    modifier = Modifier.size(24.dp),
                    contentDescription = "drawable icons",
                    colorFilter = ColorFilter.tint(Color.White)
                )
                Text(
                    text = "Se déconnecter",
                    color = Color.White,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .offset(x = (-12).dp)
                        .clickable {
                            TODO("Logout")
                        }
                )
            }
        }

    }

}