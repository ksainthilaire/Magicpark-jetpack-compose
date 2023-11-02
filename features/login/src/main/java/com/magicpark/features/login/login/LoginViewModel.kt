package com.magicpark.features.login.login

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuthException
import com.magicpark.core.Config.KEY_SHARED_PREFERENCES
import com.magicpark.domain.usecases.UserUseCases
import com.magicpark.features.login.utils.toLocaleString
import org.koin.java.KoinJavaComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.magicpark.utils.R
import kotlin.Exception

sealed interface LoginUiState {
    /**
     * Initial state.
     */
    object Loading : LoginUiState

    /**
     * The connection was successful.
     */
    object LoginSuccessful : LoginUiState

    /**
     * User login failed.
     */
    class LoginFailed(val errorMessage: String) : LoginUiState
}

class LoginViewModel : ViewModel() {

    private companion object {
        const val TAG = "RegisterViewModel"
        const val KEY_API_TOKEN = "KEY-API-TOKEN"
        const val REGEX_FULLNAME =
            "[A-Z](?=.{1,29}$)[A-Za-z]{1,}([ ][A-Z][A-Za-z]{1,})*"
    }

    private val userUseCases: UserUseCases by KoinJavaComponent
        .inject(UserUseCases::class.java)

    private val resources: Resources by KoinJavaComponent
        .inject(Resources::class.java)

    private val _state: MutableStateFlow<LoginUiState> =
        MutableStateFlow(LoginUiState.Loading)

    val state: StateFlow<LoginUiState>
        get() = _state

    /**
     * Saves the token that allows the user to authenticate with the API
     * as an already registered and logged in user.
     */
    private fun saveToken(context: Context, token: String) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(
                KEY_SHARED_PREFERENCES,
                Context.MODE_PRIVATE
            )

        sharedPreferences.edit()
            .apply {
                putString(KEY_API_TOKEN, token)
                apply()
            }
    }

    /**
     * Connects the user to the server.
     * This function is called when the user has been added in Firebase.
     *
     * @param context see [android.content.Context]
     * @param firebaseToken Token provided by Firebase Authentication after the user
     * creates an account.
     */
    fun login(context: Context, firebaseToken: String) = viewModelScope.launch {
        try {
            val token = userUseCases.login(firebaseToken)
            saveToken(context, token)

            _state.value = LoginUiState.LoginSuccessful
        } catch (e: Throwable) {
            val errorMessage = resources
                .getString(
                    R.string.common_error_unknown)
            _state.value = LoginUiState.LoginFailed(errorMessage)
        }
    }

    /**
     * Handling login errors.
     * @param exception The exception that was raised during login.
     */
    fun handleLoginException(exception: Exception) {
        viewModelScope.launch {
            val errorMessage = when (exception) {
                is FirebaseAuthException -> exception.toLocaleString()
                else -> exception.message
                    ?: resources.getString(R.string.common_error_unknown)
            }

            _state.value = LoginUiState.LoginFailed(errorMessage)
        }
    }
}
