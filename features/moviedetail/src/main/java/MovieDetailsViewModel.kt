package io.dvlt.features.moviedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.dvlt.domain.usecases.MovieUseCases
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch


class MovieDetailsViewModel : ViewModel() {

    protected val _state: MutableLiveData<State> = MutableLiveData()


    private fun onError(t: Throwable) {
        t.printStackTrace()
    }


    val state: LiveData<State>
        get() = _state

    fun getMovieDetail(id: Int) = viewModelScope.launch {
        MovieUseCases
            .getMovieDetails(id)
            .subscribeOn(Schedulers.io())
            .subscribe({ movie ->
                val state = State(isLoading = false, movie = movie)
                _state.postValue(state)
            }, ::onError)
    }

}