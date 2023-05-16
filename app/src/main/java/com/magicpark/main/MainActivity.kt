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
import com.magicpark.features.shop.ShopScreen
import com.magicpark.features.wallet.TicketScreen
import com.magicpark.features.wallet.WalletScreen
import com.magicpark.ui.webview.WebViewScreen
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {

            MagicparkMaterialTheme {


                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "/login") {

                    composable("home") {
                        WalletScreen(navController)
                    }

                    composable("/privacy-policy") {
                        val url = getString(com.magicpark.utils.R.string.privacy_policy_url)
                        WebViewScreen(navController, url)
                    }

                    composable("/support") {
                        ContactScreen(navController)
                    }


                    composable("/shop") {
                        ShopScreen(navController)
                    }


                    composable("/cart") {
                        CartScreen(navController)
                    }

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

                    composable("/account/update") {
                        UpdateAccountScreen(navController)
                    }

                    composable("/payment") {}
                    composable(
                        "/payment/{status}",
                        arguments = listOf(navArgument("status") { type = NavType.StringType })
                    ) {
                        val status = it.arguments?.getString("status") ?: "error"
                        PaymentStatus(navController, status)
                    }

                    composable("/wallet") {
                        WalletScreen(navController)
                    }
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
