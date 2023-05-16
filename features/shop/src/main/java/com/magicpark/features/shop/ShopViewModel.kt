package com.magicpark.features.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class ShopViewModel : ViewModel() {

    protected val _state: MutableLiveData<ShopState> = MutableLiveData()
    val state: LiveData<ShopState>
        get() = _state

    private fun onError(t: Throwable) {
        t.printStackTrace()
    }

}