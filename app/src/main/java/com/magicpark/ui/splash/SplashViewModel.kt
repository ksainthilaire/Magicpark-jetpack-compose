package com.magicpark.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class SplashViewModel : ViewModel() {

    private val _state: MutableStateFlow<SplashUiState> = MutableStateFlow(SplashUiState.Loading)

    private fun onError(t: Throwable) {
        t.printStackTrace()
    }

    val state: StateFlow<SplashUiState>
        get() = _state

    init {
        _state.value = SplashUiState.Loading
    }

    private fun checkUpdateRequired() = viewModelScope.launch {

    }

    private fun checkUserLoggedIn() = viewModelScope.launch {

    }

}
