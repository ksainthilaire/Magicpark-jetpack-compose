package com.magicpark.features.login

import com.facebook.login.Login


sealed class LoginState  {

    /* Login */

    object Empty : LoginState()

    object LoginSuccessful : LoginState()
    class LoginError(val message: String? = null) : LoginState()

    /* Register */
    class RegisterError(val message: String? = null): LoginState()

    class RegisterPasswordFieldError(val message: String? = null): LoginState()
    class RegisterPhoneNumberFieldError(val message: String? = null): LoginState()
    class RegisterFullNameFieldError(val message: String? = null): LoginState()

    object RegisterSuccess : LoginState()

    /* Forgot */
    object ForgotSuccessful  : LoginState()
    class ForgotError(val message: String? = null): LoginState()
}