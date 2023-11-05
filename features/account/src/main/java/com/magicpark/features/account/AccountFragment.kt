package com.magicpark.features.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
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
import androidx.fragment.app.Fragment
import com.magicpark.core.MagicparkMaterialTheme
import com.magicpark.core.MagicparkTheme
import com.magicpark.domain.model.User
import com.magicpark.utils.R
import com.magicpark.utils.ui.CallbackWithoutParameter
import dagger.hilt.android.AndroidEntryPoint
import org.koin.androidx.viewmodel.ext.android.viewModel

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private val viewModel: AccountViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext())
            .apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {

                    val state by viewModel.state.collectAsState()
                    val user by viewModel.user.collectAsState()

                    MagicparkMaterialTheme {
                        UpdateAccountScreen(
                            user,
                            updateUser = viewModel::updateUser,
                            onBackPressed = { activity?.onBackPressedDispatcher?.onBackPressed() }
                        )
                    }
                }
            }
}

@Preview
@Composable
private fun UpdateAccountScreenPreview() {
    UpdateAccountScreen(user = User(), updateUser = { s: String, s1: String, s2: String, s3: String, s4: String -> }, onBackPressed = {})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateAccountScreen(
    user: User,

    updateUser: (
        fullName: String,
        mail: String,
        password: String,
        passwordConfirmation: String,
        number: String,
    ) -> Unit,

    onBackPressed: CallbackWithoutParameter,
) {
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
                        "Mon profil",
                        fontSize = 36.sp,
                        modifier = Modifier.padding(bottom = 15.dp)
                    )

                    Image(
                        painter = painterResource(
                            R.drawable.illustration_basketball
                        ),
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .align(Alignment.BottomEnd),
                        contentDescription = null
                    )

                }

                Text(
                    modifier = Modifier
                        .wrapContentWidth(align = Alignment.Start)
                        .padding(start = MagicparkTheme.defaultPadding),
                    text = stringResource(R.string.register_label_identity),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold
                    )
                )

                OutlinedTextField(
                    modifier = Modifier.padding(top = 24.dp),
                    value = fullName,
                    singleLine = true,
                    onValueChange = { value ->
                        fullName = value
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    placeholder = { Text(user.fullName ?: "") }
                )

                OutlinedTextField(
                    modifier = Modifier.padding(top = 12.dp),
                    value = number,
                    singleLine = true,
                    onValueChange = { value ->
                        number = value
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    placeholder = { Text(user.phoneNumber ?: "") }
                )

                OutlinedTextField(
                    modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                    value = mail,
                    singleLine = true,
                    onValueChange = { value ->
                        mail = value
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    placeholder = { Text(user.mail ?: "") }
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
                        updateUser(
                            fullName,
                            mail,
                            password,
                            passwordConfirmation,
                            number
                        )
                    },
                ) {
                    Text("Modifier")
                }

                Text(
                    "[Supprimer mon compte]",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold
                    ),
                    fontSize = 12.sp,
                    color = MagicparkTheme.colors.primary,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .clickable {
                            onBackPressed()
                        }
                )
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
}
