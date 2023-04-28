package com.magicpark.app

import RegisterScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.magicpark.core.MagicparkMaterialTheme
import com.magicpark.features.account.UpdateAccountScreen
import com.magicpark.features.login.ForgotScreen
import com.magicpark.features.login.LoginScreen
import com.magicpark.features.payment.PaymentStatus
import com.magicpark.features.settings.ContactScreen
import com.magicpark.features.settings.SettingsScreen
import com.magicpark.features.shop.CartScreen
import com.magicpark.features.wallet.TicketScreen
import com.magicpark.ui.webview.WebViewScreen


class MainActivity : ComponentActivity() {


    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {

            MagicparkMaterialTheme {


                    val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "home") {
                        composable("home") {

                            CartScreen()
                           // ShopScreen(navController)

                        // WalletScreen(navController)
                            // SplashScreen(onContinue = { navController.navigate("/login") })
                        }

                        composable("/privacy-policy") {
                            val url = getString(com.magicpark.utils.R.string.privacy_policy_url)
                            WebViewScreen(navController, url)
                        }

                        composable("/support") {
                            ContactScreen(navController)
                        }


                        // Shop
                        composable("/shop") {}


                        // Login:
                        composable("/login") {
                            LoginScreen(navController)
                        }
                        composable("/register") {
                            RegisterScreen(navController)
                        }
                        composable("/forgot") {
                            ForgotScreen(navController)
                        }

                        composable("/settings") {
                            SettingsScreen(navController)
                        }

                        // User:
                        composable("/account/update") {
                            UpdateAccountScreen(navController)
                        }


                        // Payment:
                        composable("/payment") {}
                    composable("/payment/{status}",
                        arguments = listOf(navArgument("status") { type = NavType.StringType })
                    ) {
                        val status = it.arguments?.getString("status") ?: "error"
                        PaymentStatus(navController, status)
                    }

                        // Settings:


                        // Wallet:
                        composable("/wallet") {}
                        composable(
                            "/ticket/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.IntType })
                        ) {
                            TicketScreen(navController)
                        }

                    }


            }
        }

    }

}
