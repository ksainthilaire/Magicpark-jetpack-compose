package com.magicpark.features.wallet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class WalletViewModel : ViewModel() {

    protected val _state: MutableLiveData<WalletState> = MutableLiveData()


    private fun onError(t: Throwable) {
        t.printStackTrace()
    }


    val state: LiveData<WalletState>
        get() = _state


}
