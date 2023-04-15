package com.magicpark.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.magicpark.features.shop.MoviesScreen


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {

                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        MoviesScreen(onNavigateToMovie = { id -> navController.navigate("movie/$id") })
                    }
                    composable(
                        "movie/{movieId}",
                        arguments = listOf(navArgument("movieId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
                    }
                }
            }
        }

    }

}
