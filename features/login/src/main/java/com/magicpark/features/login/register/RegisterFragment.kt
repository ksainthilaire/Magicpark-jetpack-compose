package com.magicpark.features.login.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.facebook.bolts.Task
import com.hbb20.CountryCodePicker
import com.magicpark.core.MagicparkTheme
import com.magicpark.utils.R
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.magicpark.core.MagicparkMaterialTheme
import com.magicpark.features.login.forgot.ForgotUiState
import com.magicpark.features.login.login.LoginUiState
import com.magicpark.utils.ui.CallbackWithoutParameter
import com.magicpark.utils.ui.Toast
import dagger.hilt.android.AndroidEntryPoint
import org.koin.androidx.viewmodel.ext.android.viewModel


@AndroidEntryPoint
class RegisterFragment : Fragment() {

    companion object {
        private val TAG = RegisterFragment::class.java.simpleName
    }

    /**
     * @see
     * firebase.google.com/docs/reference/kotlin/com/google/firebase/auth/FirebaseAuth
     */
    private val firebaseAuth: FirebaseAuth
        get() = FirebaseAuth.getInstance()
            .apply {
                setLanguageCode("fr")
            }

    private val viewModel: RegisterViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext())
            .apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    val state by viewModel.state.collectAsState()

                    MagicparkMaterialTheme {
                        RegisterScreen(
                            state = state,
                            onBackPressed = ::onBackPressedListener,
                            goToPrivacyPolicy = { findNavController().navigate(com.magicpark.features.login.R.id.privacyFragment) },
                            register = this@RegisterFragment::register,
                        )
                    }
                }
            }


    /**
     * Check the registration fields before saving to the database.
     *
     * @param mail User email
     * @param password User password
     * @param passwordConfirmation User password confirmation
     * @param fullName User full name
     * @param phoneNumber Associated telephone number which must be the user,
     * once the registration is finished.
     * @param country Corresponds to the user's country code
     * @param termsAccepted true if the user accepts the terms of service
     */
    private fun register(
        mail: String,
        password: String,
        passwordConfirmation: String,
        fullName: String,
        phoneNumber: String,
        country: String,
        termsAccepted: Boolean,
    ) {
        try {
            viewModel.assertValidRegistrationFields(
                mail,
                password,
                passwordConfirmation,
                country,
                fullName,
                phoneNumber,
                termsAccepted
            )

            register(mail = mail, password = password, fullName = fullName, country = country, phoneNumber = phoneNumber)
        } catch (e: Exception) {
            viewModel.handleException(e)
        }
    }

    /**
     * Saves the user identifiers in Firebase, and the information associated
     * with the user in the server database.
     *
     * @param mail User email
     * @param password User password
     * @param fullName User full name
     * @param country Corresponds to the user's country code
     * @param phoneNumber Associated telephone number which must be the user,
     * once the registration is finished.
     */
    private fun register(
        mail: String,
        password: String,
        fullName: String,
        country: String,
        phoneNumber: String,
    ) {
        firebaseAuth
            .createUserWithEmailAndPassword(mail, password)
            .addOnFailureListener { exception ->
                viewModel.handleException(exception)
            }
            .addOnSuccessListener { _ ->
                val user = firebaseAuth.currentUser ?: return@addOnSuccessListener

                user
                    .getIdToken(false)
                    .addOnFailureListener { exception ->
                        viewModel.handleException(exception)
                    }
                    .addOnSuccessListener { result ->
                        val token = result.token

                        if (token == null) {
                            Log.e(
                                TAG,
                                "Unable to obtain the token associated with the user." +
                                        "mail = $mail"
                            )

                        } else {
                            Log.i(
                                TAG,
                                "The user has been successfully created on Firebase." +
                                        "mail = $mail"
                            )
                            viewModel.register(token = token, mail = mail, fullName = fullName, country = country, phoneNumber = phoneNumber)
                        }
                    }
            }
    }

    /**
     * The user wants to go back by clicking on the back button.
     */
    private fun onBackPressedListener() {
        activity?.onBackPressedDispatcher?.onBackPressed()
    }
}

@Preview
@Composable
private fun RegisterScreenPreview() =
    RegisterScreen(
        state = RegisterUiState.RegisterSuccessful,
        onBackPressed = {},
        goToPrivacyPolicy = {},
    ) { _: String,
        _: String,
        _: String,
        _: String,
        _: String,
        _: String,
        _: Boolean ->
    }


private typealias RegisterCallback = (
    mail: String,
    password: String,
    passwordConfirmation: String,
    fullName: String,
    phoneNumber: String,
    country: String,
    termsAccepted: Boolean,
) -> Unit



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    state: RegisterUiState,
    onBackPressed: CallbackWithoutParameter,
    goToPrivacyPolicy: CallbackWithoutParameter,
    register: RegisterCallback,
) = Box(modifier = Modifier.fillMaxSize()){
    val context = LocalContext.current
    val countryCodePicker: CountryCodePicker = remember {
        CountryCodePicker(context).also {
            it.setShowPhoneCode(false)
            it.showNameCode(false)
            it.showFullName(false)
            it.setCountryForPhoneCode(224)
        }
    }


    var fullName by remember { mutableStateOf("") }
    var mail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirmation by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    var termsAccepted by remember { mutableStateOf(false) }

    var passwordVisibility: Boolean by remember { mutableStateOf(false) }

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
                .padding(
                    top = 96.dp, bottom = 24.dp,
                    start = MagicparkTheme.defaultPadding,
                    end = MagicparkTheme.defaultPadding,
                )
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
                        stringResource(id = R.string.register_title),
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
                    text = stringResource(R.string.register_label_identity),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
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
                    placeholder = { Text(stringResource(id = R.string.register_label_fullname)) }
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    value = phoneNumber,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    onValueChange = { value ->
                        phoneNumber = value
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    placeholder = { Text(stringResource(id = R.string.register_label_whatsapp)) }
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    value = "",
                    singleLine = true,
                    onValueChange = {},
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    placeholder = {

                        AndroidView(
                            factory = { _ -> countryCodePicker },
                            modifier = Modifier
                                .background(Color.White)
                                .clip(RoundedCornerShape(12.dp))
                        )
                    })

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 10.dp),
                    singleLine = true,
                    value = mail,
                    onValueChange = { value ->
                        mail = value.replace(" ", "")
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    placeholder = { Text(stringResource(id = R.string.register_label_mail)) }
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

                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
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
                                    painterResource(R.drawable.password_ok)
                                else painterResource(R.drawable.password_nok),
                                contentDescription = "Show password"
                            )
                        }
                    },
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    singleLine = true,
                    visualTransformation = if (!passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None,
                    value = passwordConfirmation,
                    trailingIcon = {
                        IconButton(onClick = {
                            passwordVisibility = !passwordVisibility
                        }) {
                            Icon(
                                painter =
                                if (!passwordVisibility)
                                    painterResource(R.drawable.password_ok)
                                else painterResource(R.drawable.password_nok),
                                contentDescription = "Show password confirmation"
                            )
                        }
                    },
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

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Checkbox(
                        checked = termsAccepted,
                        onCheckedChange = { termsAccepted = it },
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
                                goToPrivacyPolicy()
                            },
                        style = TextStyle(
                            textDecoration = TextDecoration.Underline,
                            color = MagicparkTheme.colors.primary,
                        )
                    )
                }

                Button(
                    onClick = {
                            register(
                                mail,
                                password,
                                passwordConfirmation,
                                fullName,
                                phoneNumber,
                                countryCodePicker.selectedCountryCode,
                                termsAccepted
                            )
                    },
                ) {
                    Text(text = stringResource(R.string.register_button_play))
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
                                onBackPressed()
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
                    onBackPressed()
                },
            contentDescription = null,
            colorFilter = ColorFilter.tint(MagicparkTheme.colors.primary)
        )
    }

    when (state) {
        is RegisterUiState.RegisterFailed ->
            key(state) { Toast(text = state.errorMessage) }

        else -> Unit
    }
}

