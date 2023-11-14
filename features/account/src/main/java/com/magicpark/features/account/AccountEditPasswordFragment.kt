package com.magicpark.features.account

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.magicpark.core.MagicparkTheme
import com.magicpark.utils.R
import com.magicpark.utils.ui.Alert
import com.magicpark.utils.ui.CallbackWithoutParameter
import com.magicpark.utils.ui.Toast
import org.koin.androidx.compose.getViewModel

@RequiresApi(Build.VERSION_CODES.M)
@Preview
@Composable
private fun AccountEditPasswordPreview() {
    AccountEditPasswordScreen(onBackPressed = {})
}

@RequiresApi(Build.VERSION_CODES.M)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountEditPasswordScreen(
    onBackPressed: CallbackWithoutParameter,
) {

    val viewModel: AccountEditPasswordViewModel = getViewModel()
    val state by viewModel.state.collectAsState()

    var newPassword by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirmation by remember { mutableStateOf("") }


    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFCA00))
    ) {
        val (view, home, shapeTopLeft, shapeMidRight, shapeBottomLeft) = createRefs()

        LazyColumn(
            Modifier
                .padding(top = 100.dp, bottom = 20.dp)
                .constrainAs(view) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            horizontalAlignment = Alignment.CenterHorizontally

        ) {


            item {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(
                            R.drawable.shape_label_register
                        ),
                        modifier = Modifier
                            .width(300.dp)
                            .height(150.dp),
                        contentDescription = null
                    )

                    Text(
                        "Modifier mon mot de passe",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 15.dp)
                    )
                }

                if (state is AccountEditPasswordState.UserUpdateFailed)
                    Alert(
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 32.dp),
                        text = (state as AccountEditPasswordState.UserUpdateFailed).message,
                        backgroundColor = Color.Red,
                        textColor = Color.White,
                    )

                if (state is AccountEditPasswordState.UserUpdateSuccessful)
                    Alert(
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 32.dp),
                        text = stringResource(R.string.account_settings_edit_password_successful),
                        backgroundColor = Color.Green,
                        textColor = Color.White,
                    )

                Text(
                    modifier = Modifier
                        .wrapContentWidth(align = Alignment.Start)
                        .padding(start = MagicparkTheme.defaultPadding),
                    text = stringResource(R.string.register_title_password),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold
                    )
                )

                OutlinedTextField(
                    modifier = Modifier.padding(top = 12.dp),
                    value = password,
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    onValueChange = { value ->
                        password = value
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    placeholder = { Text(stringResource(id = R.string.register_label_password)) }
                )

                OutlinedTextField(
                    modifier = Modifier.padding(top = 12.dp),
                    value = newPassword,
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    onValueChange = { value ->
                        newPassword = value
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    placeholder = { Text(stringResource(id = R.string.register_label_new_password)) }
                )

                OutlinedTextField(
                    modifier = Modifier.padding(top = 12.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    value = passwordConfirmation,
                    onValueChange = { value ->
                        passwordConfirmation = value
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    placeholder = { Text(stringResource(id = R.string.register_label_password_confirmation)) }
                )

                Button(
                    modifier = Modifier.padding(top = 32.dp),
                    onClick = {
                              viewModel.updatePassword(newPassword, password, passwordConfirmation)
                    },
                ) {
                    Text("Modifier")
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
            painter = painterResource(id = R.drawable.shape_bottom_left_gold),
            modifier = Modifier
                .constrainAs(shapeBottomLeft) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                .fillMaxSize(),
            contentDescription = null
        )

        Image(
            painter = painterResource(id = R.drawable.ic_back),
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
                .constrainAs(home) {
                    top.linkTo(parent.top, 24.dp)
                    start.linkTo(parent.start, 24.dp)
                }
                .clickable {
                    onBackPressed()
                },
            contentDescription = null,
            colorFilter = ColorFilter.tint(MagicparkTheme.colors.primary)
        )
    }
}
