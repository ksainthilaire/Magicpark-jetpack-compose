package com.magicpark.features.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel



class LoginViewModel : ViewModel() {

    protected val _state: MutableLiveData<LoginState> = MutableLiveData()


    private fun onError(t: Throwable) {
        t.printStackTrace()
    }


    val state: LiveData<LoginState>
        get() = _state



}
