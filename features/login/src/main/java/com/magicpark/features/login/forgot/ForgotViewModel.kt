package com.magicpark.features.login.forgot

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthException
import com.magicpark.features.login.utils.getStringRes
import com.magicpark.utils.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent

sealed interface ForgotUiState {
    /**
     * Initial state.
     */
    object Loading : ForgotUiState

    /**
     * The forgot was successful.
     */
    object ForgotSuccessful : ForgotUiState

    /**
     * User forgot failed.
     */
    class ForgotFailed(val errorMessage: String) : ForgotUiState
}

class ForgotViewModel : ViewModel() {

    private companion object {
        private val TAG =  ForgotViewModel::class.java.simpleName
    }

    private val resources: Resources by KoinJavaComponent
        .inject(Resources::class.java)

    private val _state: MutableStateFlow<ForgotUiState> =
        MutableStateFlow(ForgotUiState.Loading)

    val state: StateFlow<ForgotUiState>
        get() = _state

    /**
     * Handling forgot errors.
     * @param exception The exception that was raised during forgot.
     */
    fun handleForgotException(exception: Exception) {
        viewModelScope.launch {
            val errorMessage = when (exception) {
                is FirebaseAuthException ->
                    resources.getString(exception.getStringRes())
                else ->
                    exception.message ?: resources.getString(R.string.common_error_unknown)
            }

            _state.value = ForgotUiState.ForgotFailed(errorMessage)
        }
    }

    fun onForgotMailSent() { _state.value = ForgotUiState.ForgotSuccessful }
}
