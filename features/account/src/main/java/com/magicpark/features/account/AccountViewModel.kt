package com.magicpark.features.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magicpark.core.Config
import com.magicpark.domain.usecases.AuthUseCases
import com.magicpark.domain.usecases.MovieUseCases
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch


class AccountViewModel : ViewModel() {

    protected val _state: MutableLiveData<AccountState> = MutableLiveData()
    val state: LiveData<AccountState>
        get() = _state

    private fun onError(t: Throwable) {
        t.printStackTrace()
    }
    
}
