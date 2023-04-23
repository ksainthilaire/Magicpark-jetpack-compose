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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.magicpark.core.MagicparkTheme
import com.magicpark.core.R
import com.magicpark.utils.ui.ErrorSnackbar

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun RegisterScreen(navController: NavController? = null) {


    var errorMessage by remember { mutableStateOf<String?>("Test") }

    var fullName by remember { mutableStateOf("") }
    var mail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirmation by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }



    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFCA00))
    ) {
        val (view, home, shapeTopLeft, shapeMidRight, shapeBottomLeft) = createRefs()


        val painter = painterResource(id = com.magicpark.core.R.drawable.shape_bottom_left_gold)
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


                Image(
                    painter = painterResource(
                        R.drawable.illustration_magicpark
                    ),
                    modifier = Modifier
                        .width(200.dp)
                        .height(200.dp),
                    contentDescription = null
                )


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
                        stringResource(id = com.magicpark.utils.R.string.register_title),
                        fontSize = 36.sp,
                        modifier = Modifier.padding(bottom = 15.dp)
                    )

                    Image(
                        painter = painterResource(
                            R.drawable.illustration_basketball
                        ),
                        modifier = Modifier
                            .padding(bottom = 15.dp)
                            .align(Alignment.BottomEnd),
                        contentDescription = null
                    )

                }


                Text(
                    modifier = Modifier
                        .wrapContentWidth(align = Alignment.Start)
                        .padding(start = MagicparkTheme.defaultPadding),
                    text = stringResource(com.magicpark.utils.R.string.register_label_identity),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(Modifier.height(10.dp))


                OutlinedTextField(
                    modifier = Modifier.padding(top = 10.dp),
                    value = fullName,
                    onValueChange = { value ->
                        fullName = value
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    placeholder = { Text(stringResource(id = com.magicpark.utils.R.string.register_label_fullname)) }
                )


                OutlinedTextField(
                    modifier = Modifier.padding(top = 10.dp),
                    value = number,
                    onValueChange = { value ->
                        number = value
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    placeholder = { Text(stringResource(id = com.magicpark.utils.R.string.register_label_whatsapp)) }
                )

                OutlinedTextField(
                    modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                    value = mail,
                    onValueChange = { value ->
                        mail = value
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    placeholder = { Text(stringResource(id = com.magicpark.utils.R.string.register_label_mail)) }
                )




                Text(
                    modifier = Modifier
                        .wrapContentWidth(align = Alignment.Start)
                        .padding(start = MagicparkTheme.defaultPadding),
                    text = stringResource(com.magicpark.utils.R.string.register_title_password),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    value = password,
                    visualTransformation = PasswordVisualTransformation(),
                    onValueChange = { value ->
                        password = value
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    placeholder = { Text(stringResource(id = com.magicpark.utils.R.string.register_label_password)) }
                )


                OutlinedTextField(
                    modifier = Modifier.padding(top = 10.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    value = passwordConfirmation,
                    onValueChange = { value ->
                        passwordConfirmation = value
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    placeholder = { Text(stringResource(id = com.magicpark.utils.R.string.register_label_password_confirmation)) }
                )

                Spacer(Modifier.height(10.dp))


                Spacer(modifier = Modifier.height(25.dp))



                Row(
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val isChecked = remember { mutableStateOf(false) }

                    Checkbox(
                        checked = isChecked.value,
                        onCheckedChange = { isChecked.value = it },
                        enabled = true,
                        colors = CheckboxDefaults.colors(
                            checkedColor = MagicparkTheme.colors.primary,
                            uncheckedColor = MagicparkTheme.colors.primary
                        )
                    )
                    Text(
                        text = "J'accepte les",
                        style = TextStyle(
                            color = MagicparkTheme.colors.primary
                        )
                    )
                    Text(
                        text = "CGU",
                        modifier = Modifier
                            .padding(start = 5.dp)
                            .clickable {
                                navController?.navigate("/privacy-policy")
                            },
                        style = TextStyle(
                            textDecoration = TextDecoration.Underline,
                            color = MagicparkTheme.colors.primary,
                        )
                    )
                }

                Button(
                    onClick = {},
                ) {
                    Text(text = stringResource(com.magicpark.utils.R.string.register_button_play))
                }


                Row(modifier = Modifier.padding(top = 10.dp)) {
                    Text(
                        "J'ai déjà un compte, ",
                        fontSize = 12.sp,
                        color = MagicparkTheme.colors.primary,
                    )

                    Text(
                        "se connecter",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline
                        ),
                        fontSize = 12.sp,
                        color = MagicparkTheme.colors.primary,
                        modifier = Modifier
                            .clickable {
                                navController?.popBackStack()
                            }
                    )
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
            painter = painterResource(id = R.drawable.ic_home),
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
            contentDescription = null,
            colorFilter = ColorFilter.tint(MagicparkTheme.colors.primary)
        )

    }

    errorMessage?.let { ErrorSnackbar(text = it) }

}