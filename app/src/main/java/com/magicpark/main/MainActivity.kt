package com.magicpark.main

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import com.magicpark.app.R
import com.magicpark.features.login.LoginActivity
import com.magicpark.features.login.Session
import com.magicpark.features.login.SessionEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val session: Session by KoinJavaComponent.inject(
        Session::class.java
    )

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!session.isConnected)
           return goToLoginActivity()

        setContentView(R.layout.activity_main)

        val navHostFragment by lazy {
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {  session.events.collect { event -> onSessionEvent(event) } }
            }
        }
    }

    private fun onSessionEvent(event: SessionEvent) {
        when (event) {
            is SessionEvent.Disconnected ->
                goToLoginActivity()
            is SessionEvent.Connected -> Unit
        }
    }

    private fun goToLoginActivity() {
        val intent = LoginActivity.intentFor(context = this)
        startActivity(intent)
    }
}
