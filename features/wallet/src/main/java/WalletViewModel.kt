package com.magicpark.features.wallet

import android.util.Log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magicpark.domain.usecases.TicketUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent

class WalletViewModel : ViewModel() {


    private val walletUseCases: TicketUseCases by KoinJavaComponent.inject(
        TicketUseCases::class.java
    )

    private val _state: MutableStateFlow<WalletState> = MutableStateFlow(WalletState.Loading)

    val state: StateFlow<WalletState>
        get() = _state

    init {
        loadWalletList()
    }

    fun loadWalletList() = viewModelScope.launch {
        val tickets = walletUseCases.getWallet()

        _state.value = WalletState.Tickets(
            inUse = tickets,
            toUse = tickets,
        )
    }

    companion object {
        const val TAG = "WalletViewModel"
    }
}
