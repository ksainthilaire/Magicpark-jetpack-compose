package com.magicpark.features.account

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magicpark.domain.model.User
import com.magicpark.domain.usecases.UserUseCases
import com.magicpark.utils.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent

sealed interface AccountState  {

    /**
     * Initial state.
     */
    object Loading : AccountState

    /**
     * User update was successful.
     */
    object UserUpdateSuccessful : AccountState

    /**
     * User update failed.
     * @param message The error message that is displayed
     */
    data class UserUpdateFailed(val message: String) : AccountState
}

class AccountViewModel : ViewModel() {


    private val resources: Resources by KoinJavaComponent
        .inject(Resources::class.java)

    private val userUseCases: UserUseCases by KoinJavaComponent.inject(UserUseCases::class.java)

    private val _user: MutableStateFlow<User> = MutableStateFlow(User())
    val user: StateFlow<User> = _user

    private val _state: MutableStateFlow<AccountState> = MutableStateFlow(AccountState.Loading)
    val state: StateFlow<AccountState> = _state

    init {
        loadUser()
    }

    /**
     * Fetch user information
     */
    private fun loadUser() = viewModelScope.launch {
        val user = userUseCases.getUser()
        _user.value = user
    }

    /**
     * Checks if all a user's fields are valid
     *
     * @param mail User email
     * @param password User password
     * @param passwordConfirmation User password confirmation
     * @param fullName User full name
     * @param phoneNumber Phone number
     */
    fun updateUser(
        mail: String,
        password: String,
        passwordConfirmation: String,
        fullName: String,
        phoneNumber: String,
    ) {

        val stringResource = when {

            password.isEmpty() ||
            passwordConfirmation.isEmpty() ->
                R.string.common_empty_fields

            fullName.isEmpty() ->
                R.string.register_error_fullname

            phoneNumber.isEmpty() ->
                R.string.register_error_wrong_number

            passwordConfirmation != password ->
                R.string.register_error_no_matching_passwords

            else -> null
        }

        if (stringResource == null) {
            updateUser(mail, fullName, phoneNumber)
            return
        }

        val errorMessage = resources.getString(stringResource)
        _state.value = AccountState.UserUpdateFailed(errorMessage)
    }

    /**
     *
     * @param mail
     * @param fullName
     * @param phoneNumber
     */

    private fun updateUser(
        mail: String,
        fullName: String,
        phoneNumber: String,
    ) = viewModelScope.launch {

        try {
            userUseCases
                .updateUser(mail, fullName, phoneNumber, null, null)

            _state.value = AccountState.UserUpdateSuccessful
        } catch (e: Exception) {
            _state.value = AccountState.UserUpdateFailed(e.localizedMessage)
        }
    }
}
