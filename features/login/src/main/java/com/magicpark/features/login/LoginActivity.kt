package com.magicpark.features.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import com.magicpark.utils.ui.Session
import com.magicpark.utils.ui.SessionEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.java.KoinJavaComponent

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {


    private val viewModel: LoginActivityViewModel by viewModel()

    private val session: Session by KoinJavaComponent.inject(
        Session::class.java
    )

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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {  viewModel.event.collect { event -> onLoginEvent(event) } }
                launch {  session.events.collect { event -> onSessionEvent(event) } }
            }
        }

        onBackPressedDispatcher
            .addCallback { navController.popBackStack() }
    }

    private fun onLoginEvent(event: LoginEvent) : Unit =
        when (event) {
            is LoginEvent.LoginSuccessful ->
                Unit
            is LoginEvent.Idle ->
                Unit
        }

    private fun onSessionEvent(event: SessionEvent) : Unit =
        when (event) {
            is SessionEvent.Disconnected -> {}
            is SessionEvent.Connected ->
                finish()
        }
}
