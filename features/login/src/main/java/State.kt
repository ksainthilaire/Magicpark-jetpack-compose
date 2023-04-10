package com.magicpark.features.moviedetail

import com.magicpark.domain.model.Movie

sealed class LoginState  {
    /* Login */
    object LoginSuccessful : LoginState()
    object LoginError : LoginState()

    /* Register */
    object RegisterFailed : LoginState()
    object RegisterSuccess : LoginState()

    /* Forgot */
    object ForgotSuccessful : LoginState()
    object ForgotError : LoginState()
}