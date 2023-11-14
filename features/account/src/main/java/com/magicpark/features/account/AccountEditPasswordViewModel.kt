package com.magicpark.features.account

import android.content.res.Resources
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.magicpark.core.connectivity.ConnectivityManager
import com.magicpark.utils.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.java.KoinJavaComponent

sealed interface AccountEditPasswordState  {

    /**
     * Initial state.
     */
    object Loading : AccountEditPasswordState

    /**
     * User update was successful.
     */
    object UserUpdateSuccessful : AccountEditPasswordState

    /**
     * User update failed.
     * @param message The error message that is displayed
     */
    data class UserUpdateFailed(val message: String) : AccountEditPasswordState

    /**
     * Internet is required to use this screen
     */
    object InternetRequired : AccountEditPasswordState
}

class AccountEditPasswordViewModel : ViewModel() {

    companion object {
        private val TAG = AccountEditPasswordViewModel::class.java.simpleName
    }

    private val firebaseAuth: FirebaseAuth
        get() = FirebaseAuth.getInstance()
            .apply {
                setLanguageCode("fr")
            }


    private val resources: Resources by KoinJavaComponent
        .inject(Resources::class.java)

    private val connectivityManager:
            ConnectivityManager by KoinJavaComponent.inject(ConnectivityManager::class.java)


    private val _state: MutableStateFlow<AccountEditPasswordState> = MutableStateFlow(AccountEditPasswordState.Loading)
    val state: StateFlow<AccountEditPasswordState> = _state

    /**
     * Checks if all a user's fields are valid
     *
     * @param newPassword New user password
     * @param password User password
     * @param passwordConfirmation User password confirmation
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun updatePassword(
        newPassword: String,
        password: String,
        passwordConfirmation: String,
    ) {

        if (!connectivityManager.hasInternet()) {
            _state.value = AccountEditPasswordState.InternetRequired
            return
        }

        val stringResource = when {

            newPassword.isEmpty() ||
            password.isEmpty() ||
            passwordConfirmation.isEmpty() ->
                R.string.common_empty_fields

            passwordConfirmation != newPassword ->
                R.string.register_error_no_matching_passwords

            else -> null
        }

        if (stringResource == null) {
            updatePassword(password = password, newPassword = newPassword)
            return
        }

        val errorMessage = resources.getString(stringResource)
        _state.value = AccountEditPasswordState.UserUpdateFailed(errorMessage)
    }

    private fun updatePassword(
        password: String,
        newPassword: String,
    ) = viewModelScope.launch {

        try {
            val user = requireNotNull(firebaseAuth.currentUser)
            val userEmail = requireNotNull(user.email)
            val credential = EmailAuthProvider.getCredential(userEmail, password)

            user.reauthenticate(credential)
                .await()

            user.updatePassword(newPassword)
                .await()

            _state.value = AccountEditPasswordState.UserUpdateSuccessful
        } catch (e: Exception) {
            Log.e(TAG, "An error occurred", e)
            _state.value = AccountEditPasswordState.UserUpdateFailed(e.localizedMessage)
        }
    }
}
