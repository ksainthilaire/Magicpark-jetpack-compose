package com.magicpark.features.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magicpark.core.Config
import com.magicpark.domain.usecases.AuthUseCases
import com.magicpark.domain.usecases.MovieUseCases
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch


class ShopViewModel : ViewModel() {

    protected val _state: MutableLiveData<ShopState> = MutableLiveData()
    val state: LiveData<ShopState>
        get() = _state

    private fun onError(t: Throwable) {
        t.printStackTrace()
    }

}