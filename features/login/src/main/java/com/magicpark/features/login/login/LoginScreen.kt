package com.magicpark.features.login.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.magicpark.core.MagicparkTheme
import com.magicpark.features.login.LoginError
import com.magicpark.features.login.LoginUiState
import com.magicpark.utils.ui.ErrorSnackbar
import kotlinx.coroutines.delay
import java.util.*

private fun handleLoginError() {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    state: LoginUiState,

    login: (username: String, password: String) -> Unit,
    loginWithGoogle: () -> Unit,
    loginWithFacebook: () -> Unit,

    onBackPressed: () -> Unit,

    goToForgot: () -> Unit,
    goToRegister: () -> Unit,
) {

    var mail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var passwordVisibility by remember { mutableStateOf(false) }

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (view, home, shapeTopLeft, shapeBottomLeft) = createRefs()

        Column(
            Modifier
                .padding(top = 20.dp, bottom = 20.dp)
                .constrainAs(view) {
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

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = mail,
                singleLine = true,
                onValueChange = { value ->
                    mail = value
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Transparent
                ),
                placeholder = { Text(stringResource(id = com.magicpark.utils.R.string.login_button_mail)) }
            )


            OutlinedTextField(
                value = password,
                singleLine = true,
                visualTransformation = if (!passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None,
                trailingIcon = {
                    IconButton(onClick = {
                        passwordVisibility = !passwordVisibility
                    }) {
                        Icon(
                            painter =
                            if (!passwordVisibility)
                                painterResource(com.magicpark.utils.R.drawable.password_ok)
                            else  painterResource(com.magicpark.utils.R.drawable.password_nok),
                            contentDescription = "Show password"
                        )
                    }
                },
                onValueChange = { value ->
                    password = value
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Transparent
                ),
                placeholder = { Text(stringResource(id = com.magicpark.utils.R.string.login_label_password)) }
            )

            Spacer(Modifier.height(10.dp))

            Text(
                stringResource(id = com.magicpark.utils.R.string.login_forgot),
                style = TextStyle(
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Bold
                ),
                fontSize = 12.sp,
                modifier = Modifier.clickable {
                                              goToForgot()
                },
                color = MagicparkTheme.colors.primary
            )

            Spacer(modifier = Modifier.height(25.dp))


            Text(
                stringResource(id = com.magicpark.utils.R.string.login_label_socials),
                fontSize = 11.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            Column(
                modifier = Modifier.padding(
                    start = MagicparkTheme.defaultPadding,
                    end = MagicparkTheme.defaultPadding
                )
            ) {

                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = MagicparkTheme.facebookButtonColor
                    ),
                    modifier = Modifier.clickable {
                        loginWithFacebook()
                    }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(com.magicpark.core.R.drawable.ic_facebook),
                        modifier = Modifier.size(24.dp),
                        contentDescription = "drawable icons"
                    )
                    Text(
                        text = stringResource(com.magicpark.utils.R.string.login_button_facebook),
                        color = MagicparkTheme.colors.primary,
                        style = TextStyle(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(1f)
                            .offset(x = (-12).dp)
                            .clickable {
                                loginWithFacebook()
                            }
                    )
                }


                Button(
                    onClick = loginWithGoogle,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {

                    Icon(
                        imageVector = ImageVector.vectorResource(com.magicpark.core.R.drawable.ic_google),
                        modifier = Modifier.size(24.dp),
                        contentDescription = "drawable icons",
                        tint = Color.Unspecified
                    )
                    Text(
                        text = stringResource(com.magicpark.utils.R.string.login_button_google),
                        color = MagicparkTheme.colors.primary,
                        style = TextStyle(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(1f)
                            .offset(x = (-12).dp)
                            .clickable {
                                loginWithGoogle()
                            }
                    )

                }
            }

            Button(
                onClick = {
                    login(mail, password)
                },
            ) {
                Text(text = stringResource(com.magicpark.utils.R.string.login_button_login))
            }



            Text(
                stringResource(id = com.magicpark.utils.R.string.login_button_register),
                style = TextStyle(
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Bold
                ),
                fontSize = 12.sp,
                color = MagicparkTheme.colors.primary,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clickable {
                        goToRegister()
                    }
            )
        }


        Image(
            painter = painterResource(id = com.magicpark.core.R.drawable.shape_top_left_yellow),
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
                .constrainAs(shapeTopLeft) {
                    top.linkTo(parent.top, (-50).dp)
                    start.linkTo(parent.start)
                },
            contentDescription = null
        )

        val painter = painterResource(id = com.magicpark.core.R.drawable.shape_bottom_left_yellow)
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



        Image(
            painter = painterResource(id = com.magicpark.core.R.drawable.ic_home),
            modifier = Modifier
                .width(30.dp)
                .height(30.dp)
                .constrainAs(home) {
                    top.linkTo(parent.top, 20.dp)
                    start.linkTo(parent.start, 20.dp)
                }
                .clickable {
                           onBackPressed()
                },
            contentDescription = null,
            colorFilter = ColorFilter.tint(MagicparkTheme.colors.primary)
        )


    }

    if (state is LoginUiState.LoginFailed) {

        val message = when (state.error) {
            is LoginError.Unknown -> ""
            is LoginError.EmptyFields -> ""
            else -> ""
        }

        ErrorSnackbar(message) {
          //  viewModel.clear()
        }

        LaunchedEffect(key1 = state) {
            delay(2000L)
        }
    }

}
