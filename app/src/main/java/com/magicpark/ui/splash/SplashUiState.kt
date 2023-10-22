package com.magicpark.ui.splash

sealed interface SplashUiState {
    object Loading : SplashUiState

    object ApplicationUpdateRequired : SplashUiState
    object ApplicationUpdateNotRequired : SplashUiState

    object UserLoggedIn : SplashUiState
    object UserNotLoggedIn : SplashUiState
}
