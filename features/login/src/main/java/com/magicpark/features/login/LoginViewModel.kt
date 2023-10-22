package com.magicpark.features.login

import android.annotation.SuppressLint
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.magicpark.domain.usecases.UserUseCases
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take

class LoginViewModel : ViewModel() {

    private companion object {
        const val TAG = "LoginViewModel"
    }

    private val userUseCases: UserUseCases by KoinJavaComponent
        .inject(UserUseCases::class.java)

    private val _state: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState.LoginScreen.Empty)

    val state: StateFlow<LoginUiState>
        get() = _state

    @SuppressLint("StaticFieldLeak")
    private lateinit var activity: AppCompatActivity

    suspend fun login(firebaseToken: String) = viewModelScope.launch {
       val token = userUseCases.login(firebaseToken)
            .single()
    }

    fun register(
        mail: String,
        password: String,
        fullName: String,
        phoneNumber: String,
    ) = viewModelScope.launch {
        Log.d(
            TAG,
            "Register with mail = ${mail}, " +
                    "fullName = ${fullName}, " +
                    "phoneNumber = ${phoneNumber}, " +
                    "mail = ${mail}, " +
                    "password = $password, "
        )

        userUseCases
            .updateUser(
                mail = mail,
                fullName = fullName,
                phoneNumber = phoneNumber,
                avatarUrl = null,
                country = null,
            ).subscribe({

            }, {})
    }

    private fun onLoginSucceeded(token: String) {
        Log.d(TAG, "Login succeeded, auth token = $token")

        _state.value = LoginUiState.LoginScreen.LoginSuccessful
    }


    private inline fun<reified T> Flow<T>.handleFirst(
        crossinline callback: suspend (T) -> Unit
    ): Flow<T> =
        take(1)
            .onEach { callback(it) }
}
