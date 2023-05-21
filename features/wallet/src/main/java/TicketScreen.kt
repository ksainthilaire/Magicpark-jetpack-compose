package com.magicpark.features.wallet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.magicpark.core.MagicparkTheme
import com.magicpark.core.R
import com.magicpark.utils.ui.ErrorSnackbar

@Composable
@Preview
fun TicketScreen(navController: NavController? = null) {


    var errorMessage by remember { mutableStateOf<String?>("La génération du QR Code a échoué") }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFCA00))
    ) {
        val (view, home, shapeTopLeft, shapeMidRight, shapeBottomLeft) = createRefs()


        val painter = painterResource(id = R.drawable.shape_bottom_left_gold)
        val imageRatio = painter.intrinsicSize.width / painter.intrinsicSize.height




        Image(

            painter = painter,
            modifier = Modifier
                .constrainAs(shapeBottomLeft) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    width = Dimension.fillToConstraints
                }
                .aspectRatio(imageRatio)
                .width(414.dp)
                .height(450.dp),
            contentScale = ContentScale.FillWidth,
            contentDescription = null
        )



        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(top = 100.dp, bottom = 20.dp)
                .constrainAs(view) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                },
            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            item {


                Text(
                    text = stringResource(com.magicpark.utils.R.string.ticket_title),
                    modifier = Modifier.padding(bottom = 20.dp),
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                BoxWithConstraints(
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.illustration_family),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentDescription = null
                    )

                    Image(
                        painter = painterResource(id = R.drawable.background_scan),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentDescription = null
                    )

                    // QR Code
                    Image(
                        painter = painterResource(id = R.drawable.background_ticket_close),
                        modifier = Modifier
                            .width(100.dp)
                            .height(100.dp),
                        contentDescription = null
                    )


                }


                Text(
                    text = stringResource(com.magicpark.utils.R.string.ticket_format),
                    modifier = Modifier.padding(top = 50.dp),
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 50.dp, end = 50.dp, top = 20.dp, bottom = 20.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(
                        Modifier
                            .background(Color.White)
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // QR Code
                        Image(
                            painter = painterResource(id = R.drawable.background_ticket_close),
                            modifier = Modifier
                                .width(80.dp)
                                .height(80.dp),
                            contentDescription = null
                        )


                        Button(
                            onClick = {
                                TODO("Download")
                            },
                        ) {
                            Text("Télécharger")
                        }

                    }
                }
            }
        }


        Image(
            painter = painterResource(id = R.drawable.shape_top_left_gold),
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
                .constrainAs(shapeTopLeft) {
                    top.linkTo(parent.top, (-50).dp)
                    start.linkTo(parent.start)
                },
            contentDescription = null
        )

        Image(
            painter = painterResource(id = R.drawable.shape_mid_gold),
            modifier = Modifier
                .constrainAs(shapeMidRight) {
                    top.linkTo(parent.top, 100.dp)
                    end.linkTo(parent.end)
                },
            contentDescription = null
        )




        Image(
            painter = painterResource(id = R.drawable.background_ticket_close),
            modifier = Modifier
                .width(30.dp)
                .height(30.dp)
                .constrainAs(home) {
                    top.linkTo(parent.top, 20.dp)
                    start.linkTo(parent.start, 20.dp)
                }
                .clickable {
                    navController?.popBackStack()
                },
            contentDescription = null
        )

    }


   // errorMessage?.let { ErrorSnackbar(text = it) }


}
