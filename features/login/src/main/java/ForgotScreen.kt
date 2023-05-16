package com.magicpark.features.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.magicpark.core.MagicparkTheme
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ForgotScreen(navController: NavController? = null) {

    val viewModel: LoginViewModel = viewModel()


    var mail by remember { mutableStateOf("") }


    Column(
        Modifier
            .padding(top = 20.dp, bottom = 20.dp),
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
            stringResource(id = com.magicpark.utils.R.string.forgot_title),
            fontSize = 24.sp,
            style = TextStyle(
                textAlign = TextAlign.Center
            )
        )


        Text(
            stringResource(id = com.magicpark.utils.R.string.forgot_text),
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 10.dp),
            style = TextStyle(
                textAlign = TextAlign.Center
            )
        )


        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = mail,
            onValueChange = { value ->
                mail = value
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.Transparent
            ),
            placeholder = { Text(stringResource(id = com.magicpark.utils.R.string.login_button_mail)) }
        )


        Spacer(modifier = Modifier.height(25.dp))


        Button(
            onClick = {
                viewModel.forgot(mail)
            },
        ) {
            Text(text = stringResource(com.magicpark.utils.R.string.forgot_button_continue))
        }


        Button(
            onClick = {
                navController?.popBackStack()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = MagicparkTheme.colors.primary
            )
        ) {
            Text(text = stringResource(com.magicpark.utils.R.string.forgot_button_cancel))
        }


    }


}