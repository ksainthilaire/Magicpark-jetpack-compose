package com.magicpark.features.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class AccountViewModel : ViewModel() {

    protected val _state: MutableLiveData<AccountState> = MutableLiveData()
    val state: LiveData<AccountState>
        get() = _state

    private fun onError(t: Throwable) {
        t.printStackTrace()
    }
    
}
