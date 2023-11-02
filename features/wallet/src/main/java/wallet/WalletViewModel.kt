package com.magicpark.features.wallet.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magicpark.domain.model.UserTicket
import com.magicpark.domain.usecases.TicketUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent

sealed interface WalletState {

    /**
     * Initial state.
     */
    object Loading : WalletState

    /**
     * List of user tickets
     * @param inUse Tickets in use
     * @param toUse Tickets already used
     * @param expired Expired tickets
     */
    data class Tickets(
        val inUse: List<UserTicket>,
        val toUse: List<UserTicket>,
        val expired: List<UserTicket>,
    ) : WalletState
}

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

    /**
     * Fetch the user's ticket list
     */

    fun loadWalletList() = viewModelScope.launch {
        val tickets = walletUseCases.getWallet()

        _state.value = WalletState.Tickets(
            inUse = tickets,
            toUse = tickets,
            expired = tickets,
        )
    }

    companion object {
        const val TAG = "WalletViewModel"
    }
}
