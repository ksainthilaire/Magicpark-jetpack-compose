package com.magicpark.features.login

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.magicpark.core.Config.SERVER_CLIENT_ID
import com.magicpark.core.MagicparkMaterialTheme
import com.magicpark.data.utils.GoogleSignInActivityContract
import com.magicpark.features.login.forgot.ForgotScreen
import com.magicpark.features.login.login.LoginScreen
import com.magicpark.features.login.register.RegisterScreen
import dagger.hilt.android.AndroidEntryPoint
import org.koin.androidx.viewmodel.ext.android.viewModel

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    companion object {
        private val TAG: String = LoginActivity::class.java.simpleName

        fun intentFor(context: Context): Intent =
            Intent(context, LoginActivity::class.java)
    }

    private val firebaseAuth: FirebaseAuth
        get() = FirebaseAuth.getInstance()

    val viewModel: LoginViewModel by viewModel()

    private val googleSignInOptions = GoogleSignInOptions
        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(
            SERVER_CLIENT_ID
        )
        .requestEmail()
        .build()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val startDestination = "/login"
        setContent {

            val state by viewModel.state.collectAsState()

            MagicparkMaterialTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = startDestination) {

                    composable("/login") {
                        LoginScreen(
                            state = state,
                            login = ::loginWithMail,
                            loginWithGoogle = ::loginWithGoogle,
                            loginWithFacebook = ::loginWithFacebook,
                            onBackPressed = navController::popBackStack,
                            goToForgot = { navController.navigate("/forgot") },
                            goToRegister = { navController.navigate("/register") },
                        )
                    }

                    composable("/register") {
                        RegisterScreen(
                            state = state,
                            onBackPressed = navController::popBackStack,
                            goToPrivacyPolicy = { navController.navigate("/privacy-policy") },
                            register = ::registerWithMail,
                        )
                    }
                    composable("/forgot") {
                        ForgotScreen(
                            state = state,
                            onBackPressed = navController::popBackStack,
                            forgot = ::forgot,
                        )
                    }
                }
            }
        }
    }

    private fun loginWithMail(
        mail: String,
        password: String
    ) {
        firebaseAuth.signInWithEmailAndPassword(mail, password)
            .addOnSuccessListener {

                val user = firebaseAuth.currentUser ?: return@addOnSuccessListener
                user
                    .getIdToken(false)
                    .addOnSuccessListener { result ->

                        val firebaseToken = result.token ?: return@addOnSuccessListener

                        Log.i(TAG, "User login with Firebase was successful. mail = $mail")
                        viewModel.login(firebaseToken)
                    }
                    .addOnFailureListener { throwable ->
                        Log.e(
                            TAG,
                            "User login with Firebase failed. mail = $mail",
                            throwable,
                        )
                    }
            }
            .addOnFailureListener { throwable ->
                Log.e(TAG, "User login with Firebase failed. mail = $mail", throwable)
            }
    }

    private fun loginWithGoogle() {
        val resultLauncher = registerForActivityResult(GoogleSignInActivityContract()) { result ->
            when (result) {
                is GoogleSignInActivityContract.Result.Success -> {
                    val googleSignInAccount = result.googleSignInAccount
                    val idToken = googleSignInAccount.idToken

                    if (idToken.isNullOrEmpty()) {
                        Log.e(TAG, "")
                        return@registerForActivityResult
                    }

                    val firebaseCredential = GoogleAuthProvider
                        .getCredential(idToken, null)

                    firebaseAuth
                        .signInWithCredential(firebaseCredential)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {

                                val currentUser = firebaseAuth.currentUser
                                    ?: return@addOnCompleteListener

                                currentUser
                                    .getIdToken(false)
                                    .addOnSuccessListener { result ->
                                        val firebaseToken = result.token
                                            ?: return@addOnSuccessListener

                                        Log.i(TAG, "User login with Google was successful." +
                                                " firebaseToken = $firebaseToken")
                                        viewModel.login(firebaseToken)
                                    }
                            } else {
                                Log.e(
                                    TAG,
                                    "User is unable to sign in with Firebase",
                                    task.exception)
                            }
                        }

                }
                is GoogleSignInActivityContract.Result.Failure -> {
                    Log.e(TAG, "The user is unable to sign in with Google.", result.exception)
                }
            }
        }

        resultLauncher.launch(googleSignInOptions)
    }

    private fun loginWithFacebook() {
        val callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    val firebaseToken = result.accessToken.token
                    Log.i(
                        TAG, "User successfully logged in with Facebook." +
                            "token = $firebaseToken")
                    viewModel.login(firebaseToken)
                }

                override fun onCancel() {
                    Log.i(TAG, "The user has canceled their connection with Facebook.")
                }

                override fun onError(error: FacebookException) {
                    Log.e(TAG, "User is unable to log in with Facebook.", error)
                }
            })

        LoginManager.getInstance()
            .logInWithReadPermissions(
                this,
                callbackManager,
                listOf("public_profile", "email")
            )
    }

    private fun registerWithMail(
        fullName: String,
        phoneNumber: String,
        mail: String,
        password: String
    ) {
        firebaseAuth.createUserWithEmailAndPassword(mail, password).addOnSuccessListener { _ ->

            val user = firebaseAuth.currentUser ?: return@addOnSuccessListener

            user
                .getIdToken(false)
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
                        viewModel.register(mail, password, fullName, phoneNumber)
                    }
                }
        }.addOnFailureListener { throwable ->
            Log.e(TAG, "User registration in Firebase failed. mail = $mail", throwable)
        }
    }


    private fun forgot(mail: String) {
        FirebaseAuth.getInstance().setLanguageCode("fr")
        FirebaseAuth.getInstance().sendPasswordResetEmail(mail)
            .addOnCompleteListener {

            }
            .addOnSuccessListener {
                Log.i(TAG, "The user has been successfully created on Firebase.")
            }.addOnFailureListener { throwable ->
                Log.i(TAG, "The user has been successfully created on Firebase.", throwable)
            }
    }
}
