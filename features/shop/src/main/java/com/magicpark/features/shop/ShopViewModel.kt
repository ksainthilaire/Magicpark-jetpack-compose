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

    protected val _state: MutableLiveData<State> = MutableLiveData()
    val state: LiveData<State>
        get() = _state

    private fun onError(t: Throwable) {
        t.printStackTrace()
    }

    fun login() = viewModelScope.launch {
        AuthUseCases
            .auth(Config.tmdbApiKeyV3)
            .subscribe({
                val state = State(isLoading = true, isInitialized = true)
                _state.postValue(state)
            }, ::onError)
    }

    private fun showProgress() = _state.postValue(State(isLoading = true))

    fun getTrending() = viewModelScope.launch {

        showProgress()

        MovieUseCases
            .getTrending()
            .subscribeOn(Schedulers.io())
            .subscribe({ list ->
                val state = State(isLoading = false, movies = list)
                _state.postValue(state)
            }, ::onError)
    }

    fun getTopRated() = viewModelScope.launch {

        showProgress()

        MovieUseCases
            .getMovieTopRated()
            .subscribeOn(Schedulers.io())
            .subscribe({ list ->
                val state = State(isLoading = false, movies = list)
                _state.postValue(state)
            }, ::onError)
    }
}