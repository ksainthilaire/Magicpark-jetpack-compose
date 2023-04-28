package com.magicpark.ui.splash
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class SplashViewModel : ViewModel() {

    protected val _state: MutableLiveData<SplashState> = MutableLiveData()

    private fun onError(t: Throwable) {
        t.printStackTrace()
    }

    val state: LiveData<SplashState>
        get() = _state

    init {
        _state.value = SplashState.Loading()
    }

    private fun checkUpdateRequired() = viewModelScope.launch {

    }

    private fun checkUserLoggedIn() = viewModelScope.launch {

    }

}