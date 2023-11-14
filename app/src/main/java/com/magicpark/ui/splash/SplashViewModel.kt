package com.magicpark.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magicpark.core.connectivity.ConnectivityManager
import com.magicpark.domain.usecases.ShopUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent


class SplashViewModel : ViewModel() {


    private val _state: MutableStateFlow<SplashUiState> = MutableStateFlow(SplashUiState.Loading)

    private val shopUseCases: ShopUseCases by KoinJavaComponent.inject(ShopUseCases::class.java)

    private val connectivityManager:
            ConnectivityManager by KoinJavaComponent.inject(ConnectivityManager::class.java)

    val state: StateFlow<SplashUiState>
        get() = _state

    init { fetchRequiredData() }


    fun fetchRequiredData() = viewModelScope.launch {
        val shopItems = shopUseCases.getShopItems() 

        if (shopItems.first.isEmpty() || shopItems.second.isEmpty()) {
            if (!connectivityManager.hasInternet()) {
                _state.value = SplashUiState.InternetRequired
                return@launch
            }
        }

        if (connectivityManager.hasInternet()) { shopUseCases.fetchShopItems() }
       _state.value = SplashUiState.Completed
    }
}
