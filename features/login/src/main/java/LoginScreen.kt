package com.magicpark.features.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import kotlinx.coroutines.NonDisposableHandle.parent
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun LoginScreen() {


    var mail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (view, shapeTopLeft, shapeBottomLeft) = createRefs()

        Image(
            painter = painterResource(id = com.magicpark.core.R.drawable.shape_top_left_yellow),
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
                .constrainAs(shapeTopLeft) {
                    top.linkTo(parent.top, -50.dp)
                    start.linkTo(parent.start)
                },
            contentDescription = null
        )


        Image(
            painter = painterResource(id = com.magicpark.core.R.drawable.shape_bottom_left_yellow),
            modifier = Modifier.constrainAs(shapeBottomLeft) {
                top.linkTo(shapeTopLeft.bottom, 20.dp)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                width = Dimension.fillToConstraints
            },
            contentDescription = null
        )




        Column(
            Modifier
                .padding(top = 20.dp, bottom = 20.dp).constrainAs(view) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                },
            horizontalAlignment = Alignment.CenterHorizontally

        ) {


            Image(
                painter = painterResource(
                    com.magicpark.core.R.drawable.illustration_magicpark
                ),
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp),
                contentDescription = null
            )

            Text(
                stringResource(id = com.magicpark.utils.R.string.login_title),
                fontSize = 36.sp
            )

            TextField(
                value = mail,
                onValueChange = { value ->
                    mail = value
                }
            )


            TextField(
                value = password,
                onValueChange = { value ->
                    password = value
                }
            )

            Spacer(Modifier.height(10.dp))

            Text(
                stringResource(id = com.magicpark.utils.R.string.login_forgot),
                style = TextStyle(textDecoration = TextDecoration.Underline),
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(25.dp))


            Text(
                stringResource(id = com.magicpark.utils.R.string.login_label_socials),
                fontSize = 11.sp
            )

            Column(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            ) {

                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(com.magicpark.core.R.drawable.ic_facebook),
                        modifier = Modifier.size(24.dp),
                        contentDescription = "drawable icons",
                        tint = Color.Unspecified
                    )
                    Text(
                        text = stringResource(com.magicpark.utils.R.string.login_button_facebook),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(1f)
                            .offset(x = -24.dp / 2) //default icon width = 24.dp
                    )
                }


                Button(onClick = {}) {

                    Icon(
                        imageVector = ImageVector.vectorResource(com.magicpark.core.R.drawable.ic_google),
                        modifier = Modifier.size(24.dp),
                        contentDescription = "drawable icons",
                        tint = Color.Unspecified
                    )
                    Text(
                        text = stringResource(com.magicpark.utils.R.string.login_button_google),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(1f)
                            .offset(x = -24.dp / 2) //default icon width = 24.dp
                    )
                }
            }

            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                )
            ) {
                Text(text = stringResource(com.magicpark.utils.R.string.login_button_login))
            }

            Text(
                stringResource(id = com.magicpark.utils.R.string.login_button_register),
                style = TextStyle(textDecoration = TextDecoration.Underline),
                fontSize = 12.sp
            )
        }
    }

}