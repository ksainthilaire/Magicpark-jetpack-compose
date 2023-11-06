package ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magicpark.domain.usecases.SupportUseCases
import com.magicpark.domain.usecases.TicketUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent

sealed interface  TicketState  {

    /**
     * Loading the settings screen.
     */
    object Loading : TicketState

    object GenerationFailed : TicketState
}

sealed interface TicketEvents {
    object TicketValided : TicketState
    object Invalid : TicketState
}


class TicketViewModel : ViewModel() {

    private val ticketUseCases: TicketUseCases by KoinJavaComponent.inject(TicketUseCases::class.java)

    private val _state: MutableStateFlow<TicketState> = MutableStateFlow(TicketState.Loading)
    val state: StateFlow<TicketState> =_state


}
