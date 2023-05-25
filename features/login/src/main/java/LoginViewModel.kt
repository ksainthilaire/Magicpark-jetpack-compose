package com.magicpark.features.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.util.Patterns
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.magicpark.domain.model.User
import com.magicpark.domain.usecases.UserUseCases
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent
import java.util.regex.Pattern
import com.magicpark.utils.R


class LoginViewModel : ViewModel() {

    private val userUseCases: UserUseCases by KoinJavaComponent.inject(UserUseCases::class.java)
    private val resources: Resources by KoinJavaComponent.inject(Resources::class.java)

    private val _state: MutableLiveData<LoginState> = MutableLiveData()

    val state: LiveData<LoginState>
        get() = _state

    @SuppressLint("StaticFieldLeak")
    private lateinit var activity: AppCompatActivity

    fun setLocalContext(appCompactActivity: Context) {
        activity = appCompactActivity as AppCompatActivity
    }

    private fun onForgotError(throwable: Throwable) {

        Log.d(TAG, "Password reset attempt failed")

        val message = when (throwable) {
            is FirebaseAuthInvalidUserException -> resources.getString(
                com.magicpark.utils.R.string.forgot_error
            )
            is java.lang.IllegalArgumentException -> resources.getString(com.magicpark.utils.R.string.common_empty_fields)
            else -> resources.getString(com.magicpark.utils.R.string.register_error)
        }

        _state.postValue(LoginState.ForgotError(message))
    }

    private fun onRegisterError(throwable: Throwable) {

        Log.d(TAG, "Registration failed ${throwable.message}")

        val message = when (throwable) {
            is java.lang.IllegalArgumentException -> resources.getString(com.magicpark.utils.R.string.common_empty_fields)
            else -> resources.getString(com.magicpark.utils.R.string.register_error)
        }

        _state.postValue(LoginState.RegisterError(message))
    }

    private fun onLoginError(throwable: Throwable) {
        Log.d(TAG, "Login failed ${throwable.message}")

        val message = when (throwable) {

            is FirebaseAuthInvalidCredentialsException -> resources.getString(com.magicpark.utils.R.string.login_failed)
            is java.lang.IllegalArgumentException -> resources.getString(com.magicpark.utils.R.string.common_empty_fields)
            else -> resources.getString(com.magicpark.utils.R.string.register_error)

        }

        _state.postValue(LoginState.LoginError(message))
    }

    private fun onLoginSucceeded(user: User) {
        Log.d(TAG, "Login succeeded ${user.fullName} (${user.id})")
        _state.postValue(LoginState.LoginSuccessful)
    }

    private fun onForgotSucceeded() {
        Log.d(TAG, resources.getString(com.magicpark.utils.R.string.forgot_mail_sent))
        _state.postValue(LoginState.ForgotSuccessful)
        _state.postValue(LoginState.Empty)
    }

    fun login(mail: String, password: String) = viewModelScope.launch {
        Log.d(TAG, "Login with mail = ${mail}, password = $password")

        userUseCases.loginWithMail(activity, mail, password)
            .subscribe(::onLoginSucceeded, ::onLoginError)
    }


    fun loginWithFacebook() = viewModelScope.launch {
        Log.d(TAG, "Login with Facebook")

        userUseCases.loginWithFacebook(activity)
            .subscribe(::onLoginSucceeded, ::onLoginError)
    }

    fun loginWithGoogle() = viewModelScope.launch {

        Log.d(TAG, "Login with Google")

        userUseCases.loginWithGoogle(activity)
            .subscribe(::onLoginSucceeded, ::onLoginError)
    }


    private fun isValidEmail(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }


    val REGEX_FULLNAME = "[A-Z](?=.{1,29}$)[A-Za-z]{1,}([ ][A-Z][A-Za-z]{1,})*";


    private fun showRegisterError(@StringRes id: Int): Unit =
        _state.postValue(LoginState.RegisterError(resources.getString(id)))

    private fun showForgotError(@StringRes id: Int): Unit =
        _state.postValue(LoginState.ForgotError(resources.getString(id)))


    fun register(
        mail: String,
        password: String,
        passwordConfirmation: String,
        fullName: String,
        phoneNumber: String,
        country: String,
        cgvChecked: Boolean
    ) = viewModelScope.launch {
        Log.d(
            TAG,
            "Register with mail = ${mail}, fullName = ${fullName}, phoneNumber = ${phoneNumber}, mail = ${mail}, password = $password, country = ${country}"
        )

        val pattern = Pattern.compile(REGEX_FULLNAME)
        val matcher = pattern.matcher(fullName)

        if (mail.isEmpty()
            || password.isEmpty()
            || passwordConfirmation.isEmpty()
            || fullName.isEmpty()
            || phoneNumber.isEmpty()
        )
            return@launch showRegisterError(R.string.common_empty_fields)

        if (!matcher.matches()) return@launch showRegisterError(R.string.register_error_fullname)
        if (!isValidEmail(mail)) return@launch showRegisterError(R.string.register_error_wrong_email)
        if (passwordConfirmation != password) return@launch showRegisterError(R.string.register_error_no_matching_passwords)
        if (!cgvChecked) return@launch showRegisterError(R.string.register_error_terms_not_accepted)

        userUseCases.registerWithMail(activity, fullName, phoneNumber, mail, password)
            .subscribe(::onLoginSucceeded, ::onRegisterError)
    }

    fun forgot(mail: String) = viewModelScope.launch {

        Log.d(TAG, "Email reset request = $mail")

        if (!isValidEmail(mail)) return@launch showForgotError(R.string.register_error_wrong_email)

        userUseCases.forgot(mail)
            .subscribe(::onForgotSucceeded, ::onForgotError)
    }


    fun clear() {
        _state.postValue(LoginState.Empty)
    }

    companion object {
        const val TAG = "LoginViewModel"
    }
}
