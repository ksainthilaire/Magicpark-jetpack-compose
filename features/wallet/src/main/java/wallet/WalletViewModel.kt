package wallet

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magicpark.domain.model.UserTicket
import com.magicpark.domain.model.isExpired
import com.magicpark.domain.usecases.TicketUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent
import java.util.Calendar
import java.util.Date

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

    init { fetchWallet() }

    /**
     * Fetch the user's ticket list
     */
    private fun fetchWallet() = viewModelScope.launch {
        val tickets = walletUseCases.getWallet()

        Log.i(TAG, "Fetch the user's ticket list. tickets = $tickets.")

        _state.value = WalletState.Tickets(
            inUse = tickets.getInUse(),
            toUse = tickets.filter { ticket -> !ticket.isExpired() },
            expired = tickets.filter { ticket -> ticket.isExpired() },
        )
    }

    private fun List<UserTicket>.getInUse() : List<UserTicket> = this

    companion object {
        const val TAG = "WalletViewModel"
    }
}
