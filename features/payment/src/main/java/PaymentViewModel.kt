package com.magicpark.features.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class PaymentViewModel : ViewModel() {

    protected val _state: MutableLiveData<PaymentState> = MutableLiveData()


    private fun onError(t: Throwable) {
        t.printStackTrace()
    }


    val state: LiveData<PaymentState>
        get() = _state
    

}
