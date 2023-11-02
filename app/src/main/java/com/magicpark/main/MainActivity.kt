package com.magicpark.app

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.magicpark.features.shop.Cart
import com.magicpark.features.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import org.koin.java.KoinJavaComponent

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val magicparkDbSession: Cart by KoinJavaComponent.inject(
        Cart::class.java
    )



    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        startLoginActivity()
/*
        val startDestination = if (magicparkDbSession.isLogged()) "/shop" else {
            if (magicparkDbSession.getWelcome()) return startLoginActivity()
            else "/splash"
        }
*/
        val startDestination = "/shop"
        setContent {


        }

    }

    private fun startLoginActivity() {
        val intent = LoginActivity.intentFor(context = this)
        startActivity(intent)
    }
}
