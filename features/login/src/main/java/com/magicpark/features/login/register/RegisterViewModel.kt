package com.magicpark.features.login.register

import android.content.res.Resources
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuthException
import com.magicpark.domain.usecases.UserUseCases
import com.magicpark.features.login.utils.getStringRes
import org.koin.java.KoinJavaComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.magicpark.utils.R
import com.magicpark.utils.ui.Session
import java.util.regex.Pattern
import kotlin.Exception

sealed interface RegisterUiState {
    /**
     * Initial state.
     */
    object Loading : RegisterUiState

    /**
     * The connection was successful.
     */
    object RegisterSuccessful : RegisterUiState

    /**
     * User login failed.
     */
    class RegisterFailed(val errorMessage: String) : RegisterUiState
}

class RegisterViewModel : ViewModel() {

    private companion object {
        const val TAG = "RegisterViewModel"
        const val REGEX_FULLNAME =
            "[A-Z](?=.{1,29}$)[A-Za-z]{1,}([ ][A-Z][A-Za-z]{1,})*"
    }

    private val session: Session by KoinJavaComponent
        .inject(Session::class.java)

    private val userUseCases: UserUseCases by KoinJavaComponent
        .inject(UserUseCases::class.java)

    private val resources: Resources by KoinJavaComponent
        .inject(Resources::class.java)

    private val _state: MutableStateFlow<RegisterUiState> =
        MutableStateFlow(RegisterUiState.Loading)

    val state: StateFlow<RegisterUiState>
        get() = _state


    /**
     * This function is called when the user has already been registered in Firebase.
     *
     * @param mail Mail
     * @param fullName Full name
     * @param phoneNumber Phone number
     * @param country Country code
     */
    fun register(
        token: String,
        mail: String,
        fullName: String,
        country: String,
        phoneNumber: String,
    ) = viewModelScope.launch {

        Log.i(
            TAG,
            "Register with mail = ${mail}, " +
                    "fullName = ${fullName}, " +
                    "phoneNumber = ${phoneNumber}, " +
                    "mail = ${mail}, " +
                    "country = $country."
        )

        try {

            val sessionToken = userUseCases.login(token)
            session.saveToken(sessionToken)

            val user = userUseCases.getUser()
            session.saveUserData(user)

            userUseCases
                .updateUser(
                    mail = mail,
                    fullName = fullName,
                    phoneNumber = phoneNumber,
                    avatarUrl = null,
                    country = country,
                    password = null,
                )

            _state.value = RegisterUiState.RegisterSuccessful
        } catch (throwable: Throwable) {
            Log.e(TAG, "Unable to continue user registration. mail = $mail.", throwable)

            val errorMessage = throwable.localizedMessage ?: resources
                .getString(R.string.common_error_unknown)
            _state.value = RegisterUiState.RegisterFailed(errorMessage)
        }
    }

    private fun String.isValidEmail(): Boolean {
        if (this.isEmpty())
            return false

        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern
            .matcher(this)
            .matches()
    }

    /**
     * Checks if all a user's registration fields are valid, otherwise an exception is thrown.
     *
     * @param mail User email
     * @param password User password
     * @param passwordConfirmation User password confirmation
     * @param country Corresponds to the user's country code
     * @param fullName User full name
     * @param phoneNumber Associated telephone number which must be the user,
     * once the registration is finished.
     * @param termsAccepted true if the user accepts the terms of service
     */
    fun assertValidRegistrationFields(
        mail: String,
        password: String,
        passwordConfirmation: String,
        country: String,
        fullName: String,
        phoneNumber: String,
        termsAccepted: Boolean,
    ) {
        val pattern = Pattern.compile(REGEX_FULLNAME)
        val matcher = pattern.matcher(fullName)

        val stringResource = when {

            fullName.isEmpty() || passwordConfirmation.isEmpty() || mail.isEmpty() ->
                R.string.common_empty_fields

            fullName.split("\\s+".toRegex()).count() < 2 ->
                R.string.register_error_fullname

            phoneNumber.isEmpty() ->
                R.string.register_error_wrong_number

            !mail.isValidEmail() ->
                R.string.register_error_wrong_email

            password.isEmpty() || (password.length < 12) ->
                R.string.register_error_password_rules


            //!matcher.matches() -> R.string.register_error_fullname

            passwordConfirmation != password ->
                R.string.register_error_no_matching_passwords

            !termsAccepted ->
                R.string.register_error_terms_not_accepted

            else -> null
        }

        stringResource?.let { idRes ->
            val message = resources.getString(idRes)
            throw Exception(message)
        }
    }


    /**
     * Handling login errors.
     * @param exception The exception that was raised during login.
     */
    fun handleException(exception: Exception) {
        viewModelScope.launch {
            val errorMessage = when (exception) {
                is FirebaseAuthException -> resources.getString(exception.getStringRes())
                else -> exception.message
                    ?: resources.getString(R.string.common_error_unknown)
            }

            _state.value = RegisterUiState.RegisterFailed(errorMessage)
        }
    }
}
