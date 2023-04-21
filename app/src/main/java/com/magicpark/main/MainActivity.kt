package com.magicpark.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.magicpark.features.login.LoginScreen
import com.magicpark.features.shop.MoviesScreen
import com.magicpark.ui.splash.SplashScreen
import com.magicpark.ui.webview.WebViewScreen


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Blue),
                color = MaterialTheme.colorScheme.background
            ) {

                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        SplashScreen(onContinue = { navController.navigate("/login") } )
                    }

                    composable("/privacy-policy") {
                        val url = getString(com.magicpark.utils.R.string.privacy_policy_url)
                        WebViewScreen(url)
                    }


                    // Shop
                    composable("/shop") {}


                    // Login:
                    composable("/login") {
                        LoginScreen()
                    }
                    composable("/register") {}
                    composable("/forgot") {}


                    // Payment:
                    composable("/payment") {}
                    composable("/after-payment") {}

                    // Settings:


                    // Wallet:
                    composable("/wallet") {}
                    composable("/ticket/{id}", arguments = listOf(navArgument("id") { type = NavType.IntType })) {}

                }


            }
        }

    }

}
