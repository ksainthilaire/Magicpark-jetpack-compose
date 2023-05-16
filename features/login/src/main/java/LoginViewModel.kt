package com.magicpark.features.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel


@HiltViewModel
class LoginViewModel : ViewModel() {

    protected val _state: MutableLiveData<LoginState> = MutableLiveData()


    private fun onError(t: Throwable) {
        t.printStackTrace()
    }


    val state: LiveData<LoginState>
        get() = _state



    fun loginWithFacebook() {

    }

    fun loginWithGoogle() {

    }

    fun login(mail: String, password: String) {
        Log.d(TAG, "Login with mail = ${mail}, password = ${password}")


    }

    fun forgot(mail: String) {
        Log.d(TAG, "Email reset request = ${mail}")
    }

    companion object {
        val TAG = "LoginViewModel"
    }
}
