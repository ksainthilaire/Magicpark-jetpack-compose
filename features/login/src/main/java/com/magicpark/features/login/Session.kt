package com.magicpark.features.login

import android.content.Context
import android.content.SharedPreferences
import com.magicpark.core.Config
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
sealed interface SessionEvent {
    object Disconnected : SessionEvent
    object Connected : SessionEvent
}
class Session(val context: Context) {
    companion object {
        const val KEY_API_TOKEN = "KEY-API-TOKEN"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(
            Config.KEY_SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )

    private val coroutineScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main)

    private val _events: MutableSharedFlow<SessionEvent> = MutableSharedFlow()
    val events: SharedFlow<SessionEvent> = _events
    val isConnected: Boolean
        get() = sharedPreferences.getString(KEY_API_TOKEN, "")?.isNotEmpty() ?: false

    /**
     * Saves the token that allows the user to authenticate with the API
     * as an already registered and logged in user.
     */
    fun saveToken(token: String) {
        sharedPreferences.edit()
            .putString(KEY_API_TOKEN, token)
            .apply()

        coroutineScope.launch {
            _events.emit(SessionEvent.Connected)
        }
    }
    fun removeToken() {
        sharedPreferences
            .edit()
            .remove(KEY_API_TOKEN)
            .apply()

        coroutineScope.launch {
            _events.emit(SessionEvent.Disconnected)
        }
    }
}
