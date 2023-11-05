package com.magicpark.features.login

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import com.magicpark.domain.usecases.UserUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.java.KoinJavaComponent

sealed interface LoginEvent {
    /**
     * Initial event.
     */
    object Idle : LoginEvent

    /**
     * The connection was successful.
     */
    object LoginSuccessful : LoginEvent
}

class LoginActivityViewModel : ViewModel() {

    private companion object {
        const val TAG = "RegisterViewModel"
        const val KEY_API_TOKEN = "KEY-API-TOKEN"
        const val REGEX_FULLNAME =
            "[A-Z](?=.{1,29}$)[A-Za-z]{1,}([ ][A-Z][A-Za-z]{1,})*"
    }

    private val userUseCases: UserUseCases by KoinJavaComponent
        .inject(UserUseCases::class.java)

    private val resources: Resources by KoinJavaComponent
        .inject(Resources::class.java)

    private val _event: MutableStateFlow<LoginEvent> =
        MutableStateFlow(LoginEvent.Idle)

    val event: StateFlow<LoginEvent>
        get() = _event

}
