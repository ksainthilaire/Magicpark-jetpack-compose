package com.magicpark.ui.splash

sealed interface SplashUiState {
    object Loading : SplashUiState

    /**
     * An update is required to continue
     */
    object ApplicationUpdateRequired : SplashUiState

    /**
     * Internet is required to use this screen
     */
    object InternetRequired : SplashUiState

    object Completed : SplashUiState
}
