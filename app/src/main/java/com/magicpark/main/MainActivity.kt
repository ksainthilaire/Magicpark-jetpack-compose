package com.magicpark.app

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.magicpark.core.MagicparkMaterialTheme
import com.magicpark.features.shop.Cart
import com.magicpark.features.account.UpdateAccountScreen
import com.magicpark.features.login.LoginActivity
import com.magicpark.features.payment.PaymentStatus
import com.magicpark.features.settings.ContactScreen
import com.magicpark.features.settings.SettingsScreen
import com.magicpark.features.settings.SettingsViewModel
import com.magicpark.features.shop.CartScreen
import com.magicpark.features.shop.ShopItemScreen
import com.magicpark.features.shop.ShopScreen
import com.magicpark.features.shop.ShopViewModel
import com.magicpark.features.wallet.TicketScreen
import com.magicpark.features.wallet.WalletScreen
import com.magicpark.features.wallet.WalletViewModel
import com.magicpark.ui.splash.SplashScreen
import com.magicpark.ui.webview.WebViewScreen
import dagger.hilt.android.AndroidEntryPoint
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.java.KoinJavaComponent


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val magicparkDbSession: Cart by KoinJavaComponent.inject(
        Cart::class.java
    )

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        val startDestination = if (magicparkDbSession.isLogged()) "/shop" else {
            if (magicparkDbSession.getWelcome()) return startLoginActivity()
            else "/splash"
        }

        setContent {

            MagicparkMaterialTheme {


                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = startDestination) {

                    composable("/splash") {
                        magicparkDbSession.setWelcome()
                        SplashScreen {
                            navController.navigate("/login")
                        }
                    }

                    composable("/home") {

                        val viewModel: WalletViewModel by viewModel()
                        WalletScreen(navController, viewModel)
                    }

                    composable("/privacy-policy") {
                        val url = getString(com.magicpark.utils.R.string.privacy_policy_url)
                        WebViewScreen(navController, url)
                    }

                    composable("/support") {
                        val viewModel: SettingsViewModel by viewModel()
                        ContactScreen(navController, viewModel)
                    }


                    composable("/shop") {
                        val viewModel: ShopViewModel by viewModel()
                        ShopScreen(navController, viewModel)
                    }

                    composable("/shop/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.LongType }),
                        content = {
                            val viewModel: ShopViewModel by viewModel()
                            val id = it.arguments?.getLong("id") ?: 0
                            ShopItemScreen(navController, viewModel, id)
                        })

                    composable("/cart") {
                        val viewModel: ShopViewModel by viewModel()
                        CartScreen(navController, viewModel)
                    }

                    composable("/settings") {
                        val viewModel: SettingsViewModel by viewModel()
                        SettingsScreen(navController, viewModel, this@MainActivity)
                    }

                    composable("/account/update") {
                        val viewModel: SettingsViewModel by viewModel()
                        UpdateAccountScreen(navController, viewModel)
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
                        val viewModel: WalletViewModel by viewModel()
                        WalletScreen(navController, viewModel)
                    }

                    composable(
                        "/ticket/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.LongType })
                    ) {
                        val id = it.arguments?.getLong("id") ?: 0
                        val viewModel: WalletViewModel by viewModel()
                        TicketScreen(navController, viewModel, id)
                    }
                }
            }
        }

    }

    private fun startLoginActivity() {
        val intent = LoginActivity.intentFor(context = this)
        startActivity(intent)
    }
}
