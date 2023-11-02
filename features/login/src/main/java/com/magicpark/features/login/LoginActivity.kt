package com.magicpark.features.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    companion object {
        private val TAG: String = LoginActivity::class.java.simpleName

        /**
         * Create an intent to launch the LoginActivity.
         * @param context see [android.content.Context]
         */
        fun intentFor(context: Context): Intent =
            Intent(context, LoginActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val navHostFragment by lazy {
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        }
        val navController = navHostFragment.navController

        onBackPressedDispatcher
            .addCallback {
                navController.popBackStack()
            }
    }
}
