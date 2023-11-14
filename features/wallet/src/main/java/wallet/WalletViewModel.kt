package wallet

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magicpark.domain.model.UserTicket
import com.magicpark.domain.model.isExpired
import com.magicpark.domain.usecases.TicketUseCases
import com.magicpark.utils.ui.Session
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
     * Internet is required to use this screen.
     */
    object InternetRequired : WalletState

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

    object WalletEmpty : WalletState
}

@RequiresApi(Build.VERSION_CODES.M)
class WalletViewModel(private val context: Context) : ViewModel() {


    private val walletUseCases: TicketUseCases by KoinJavaComponent.inject(
        TicketUseCases::class.java
    )

    private val session: Session by KoinJavaComponent.inject(
        Session::class.java
    )

    private val _state: MutableStateFlow<WalletState> = MutableStateFlow(WalletState.Loading)

    val state: StateFlow<WalletState>
        get() = _state

    private val userId: Long
        get() {
            val user = session.getUserData()
            return requireNotNull(user.id)
        }

    init { checkWallet() }

    /**
     * Fetch the user's ticket list
     */
    private fun fetchWallet() = viewModelScope.launch {
        val tickets = walletUseCases.fetchWallet(userId = userId)

        Log.i(TAG, "Fetch the user's ticket list. tickets = $tickets.")

        _state.value = tickets.toWalletState()
    }

    private fun List<UserTicket>.getInUse() : List<UserTicket> = this

    private fun List<UserTicket>.toWalletState() : WalletState {

        if (isEmpty()) return WalletState.WalletEmpty

        return WalletState.Tickets(
            inUse = getInUse(),
            toUse = filter { ticket -> !ticket.isExpired() },
            expired = filter { ticket -> ticket.isExpired() },
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    fun hasInternet(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkWallet() = viewModelScope.launch {
        val tickets = walletUseCases.getWallet(userId)

        if (tickets.isEmpty()) {
            if (hasInternet()) { fetchWallet() }
            else { _state.value = WalletState.InternetRequired }

            return@launch
        }

        if (hasInternet()) { fetchWallet() }
        else { _state.value = tickets.toWalletState() }
    }

    companion object {
        const val TAG = "WalletViewModel"
    }
}
