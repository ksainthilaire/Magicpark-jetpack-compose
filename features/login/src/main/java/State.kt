package com.magicpark.features.login



sealed class LoginState  {
    /* Login */
    object LoginSuccessful : LoginState()
    class LoginError(val message: String? = null) : LoginState()

    /* Register */
    class RegisterFailed(val message: String? = null): LoginState()
    object RegisterSuccess : LoginState()

    /* Forgot */
    class ForgotSuccessful(val message: String? = null) : LoginState()
    object ForgotError : LoginState()
}