package com.magicpark.features.login.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
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
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.magicpark.core.Config
import com.magicpark.core.MagicparkTheme
import com.magicpark.data.utils.GoogleSignInActivityContract
import com.magicpark.features.login.R
import com.magicpark.utils.ui.ErrorSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

@AndroidEntryPoint
class LoginFragment : Fragment() {

    companion object {
        private val TAG = LoginFragment::class.java.simpleName
    }

    private val viewModel: LoginViewModel by viewModel()

    /**
     * @see
     * firebase.google.com/docs/reference/kotlin/com/google/firebase/auth/FirebaseAuth
     */
    private val firebaseAuth: FirebaseAuth
        get() = FirebaseAuth.getInstance()
            .apply {
                setLanguageCode("fr")
            }

    /**
     * @see
     * developers.google.com
     * /android/reference/com/google/android/gms/auth/api/signin/GoogleSignInOptions
     */
    private val googleSignInOptions = GoogleSignInOptions
        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(
            Config.SERVER_CLIENT_ID
        )
        .requestEmail()
        .build()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext())
            .apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    val state by viewModel.state.collectAsState()

                    LoginScreen(
                        state = state,
                        login = ::loginWithMail,
                        loginWithGoogle = ::loginWithGoogle,
                        loginWithFacebook = ::loginWithFacebook,
                        onBackPressed = ::onBackPressedListener,
                        goToForgot = {
                            findNavController().navigate(R.id.forgotFragment)
                                     },
                        goToRegister = {  findNavController().navigate(R.id.registerFragment) },
                    )
                }
            }

    /**
     * The user wants to log in using their email address and password.
     *
     * @param mail User email
     * @param password User password
     */
    private fun loginWithMail(
        mail: String,
        password: String,
    ) {
        val context = requireContext()

        firebaseAuth.signInWithEmailAndPassword(mail, password)
            .addOnFailureListener { exception ->
                viewModel.handleLoginException(exception)
            }
            .addOnSuccessListener {

                val user = firebaseAuth.currentUser ?: return@addOnSuccessListener
                user
                    .getIdToken(false)
                    .addOnFailureListener { exception ->
                        viewModel.handleLoginException(exception)
                    }
                    .addOnSuccessListener { result ->
                        val firebaseToken = result.token ?: return@addOnSuccessListener
                        Log.i(TAG, "User login with Firebase was successful. mail = $mail")
                        viewModel.login(context, firebaseToken)
                    }
            }
    }

    /**
     * The user wants to register or log in with their Google credentials.
     */
    private fun loginWithGoogle() =
        activityResultLauncher.launch(googleSignInOptions)

    /**
     * The user wants to register or log in with their Facebook credentials.
     */
    private fun loginWithFacebook() {
        val context = requireContext()
        val callbackManager = CallbackManager.Factory.create()

        LoginManager
            .getInstance()
            .registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult) {
                        val firebaseToken = result.accessToken.token
                        Log.i(
                            TAG, "User successfully logged in with Facebook." +
                                    "token = $firebaseToken"
                        )

                        viewModel.login(context, firebaseToken)
                    }

                    override fun onCancel() {
                        Log.i(TAG, "The user has canceled their connection with Facebook.")
                    }

                    override fun onError(error: FacebookException): Unit =
                        viewModel.handleLoginException(error)
                })

        LoginManager
            .getInstance()
            .logInWithReadPermissions(
                this,
                callbackManager,
                listOf("public_profile", "email")
            )
    }

    private val activityResultLauncher = registerForActivityResult(
        GoogleSignInActivityContract()
    ) { result ->

        val context = requireContext()

        when (result) {

            is GoogleSignInActivityContract.Result.Success -> {
                val googleSignInAccount = result.googleSignInAccount
                val idToken = googleSignInAccount.idToken

                if (idToken.isNullOrEmpty())
                    return@registerForActivityResult

                val firebaseCredential = GoogleAuthProvider
                    .getCredential(idToken, null)

                firebaseAuth
                    .signInWithCredential(firebaseCredential)
                    .addOnFailureListener { exception ->
                        viewModel.handleLoginException(exception)
                    }
                    .addOnSuccessListener {
                        val currentUser =
                            firebaseAuth.currentUser ?: return@addOnSuccessListener

                        currentUser
                            .getIdToken(false)
                            .addOnFailureListener { exception ->
                                viewModel.handleLoginException(exception)
                            }
                            .addOnSuccessListener { result ->
                                val firebaseToken = result.token
                                    ?: return@addOnSuccessListener

                                Log.i(
                                    TAG, "User login with Google was successful." +
                                            " firebaseToken = $firebaseToken"
                                )

                                viewModel.login(context, firebaseToken)
                            }
                    }
            }

            is GoogleSignInActivityContract.Result.Failure -> {
                Log.e(TAG, "The user is unable to sign in with Google.", result.exception)
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
            val fieldsNotEmpty = mail.isNotEmpty() && password.isNotEmpty()
            Button(
                enabled = fieldsNotEmpty,
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

        ErrorSnackbar(state.errorMessage) {}

        LaunchedEffect(key1 = state) {
            delay(2000L)
        }
    }

}
