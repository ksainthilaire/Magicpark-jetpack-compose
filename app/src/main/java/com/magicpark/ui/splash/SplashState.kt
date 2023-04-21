package com.magicpark.ui.splash

sealed class SplashState {

    class Loading(val isLoading: Boolean = false) : SplashState()

    object ApplicationUpdateRequired : SplashState()
    object ApplicationUpdateNotRequired : SplashState()

    object UserLoggedIn : SplashState()
    object UserNotLoggedIn : SplashState()
}