package ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magicpark.domain.usecases.SupportUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent

sealed interface  TicketState  {

    /**
     * Loading the settings screen.
     */
    object Loading : TicketState

    /**
     * Bug report sent to the server.
     */
    object HelpRequestSent : TicketState

    /**
     * Sending the bug report to the server failed.
     * @param message bug report message.
     */
    class HelpRequestError(private val message: String? = null) : TicketState
}


class TicketViewModel : ViewModel() {

    companion object {
        const val KEY_API_TOKEN = "KEY-API-TOKEN"
    }

    private val supportUseCases: SupportUseCases by KoinJavaComponent.inject(SupportUseCases::class.java)

    private val _state: MutableStateFlow<TicketState> = MutableStateFlow(TicketState.Loading)
    val state: StateFlow<TicketState> =_state

    /**
     * Send a bug report to the server.
     * @param text Bug report message.
     */
    fun sendBugReport(text: String) = viewModelScope.launch {
        try {
            supportUseCases
                .help(text)

            _state.value = TicketState.HelpRequestSent
        }
        catch (e: Exception) {
            _state.value = TicketState.HelpRequestError(e.message)
        }
    }
}
