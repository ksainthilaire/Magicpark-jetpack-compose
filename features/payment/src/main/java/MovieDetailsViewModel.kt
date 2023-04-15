package com.magicpark.features.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magicpark.domain.usecases.MovieUseCases
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch


class MovieDetailsViewModel : ViewModel() {

    protected val _state: MutableLiveData<PaymentState> = MutableLiveData()


    private fun onError(t: Throwable) {
        t.printStackTrace()
    }


    val state: LiveData<PaymentState>
        get() = _state
    

}
