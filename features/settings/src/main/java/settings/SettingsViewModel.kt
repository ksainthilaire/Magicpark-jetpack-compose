package settings

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magicpark.core.connectivity.ConnectivityManager
import com.magicpark.domain.model.User
import com.magicpark.domain.usecases.UserUseCases
import com.magicpark.utils.ui.Session
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent

sealed interface SettingsState {

    /**
     * Loading the settings screen.
     */
    object Loading : SettingsState

    /**
     * User logout failed.
     */
    object LogoutFailed : SettingsState

    /**
     * User logout was successful.
     */
    object LogoutSucceeded : SettingsState

    /**
     * Account deletion is in progress.
     */
    object AccountDeletingProgress : SettingsState

    /**
     * User deletion was successful
     */
    object AccountDeletionSucceeded : SettingsState

    /**
     * User deletion failed
     */
    object AccountDeletionFailed : SettingsState

    /**
     * Internet is required to use this screen
     */
    object InternetRequired : SettingsState
}


class SettingsViewModel : ViewModel() {

    companion object {
        private val TAG = SettingsViewModel::class.java.simpleName
    }

    private val userUseCases: UserUseCases by KoinJavaComponent.inject(UserUseCases::class.java)

    private val session: Session by KoinJavaComponent.inject(
        Session::class.java
    )

    private val connectivityManager:
            ConnectivityManager by KoinJavaComponent.inject(ConnectivityManager::class.java)

    private val _user: MutableStateFlow<User> = MutableStateFlow(User())
    val user: StateFlow<User> = _user

    private val _state: MutableStateFlow<SettingsState> = MutableStateFlow(SettingsState.Loading)
    val state: StateFlow<SettingsState> = _state

    init {
        loadUser()
    }

    /**
     * Fetch user information
     */
    private fun loadUser() = viewModelScope.launch {
        val user = session.getUserData()
        _user.value = user
    }

    /**
     * Update user information.
     */
    fun updateUser(
        avatarUrl: String,
    ) = viewModelScope.launch {

        userUseCases
            .updateUser(avatarUrl = avatarUrl)
    }

    /**
     * Logs out the current user.
     * @param context see [android.content.Context]
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun logout(context: Context) = viewModelScope.launch {

        if (!connectivityManager.hasInternet()) {
            _state.value = SettingsState.InternetRequired
            return@launch
        }

        try {
            session.removeToken()
            userUseCases.logout()

            _state.value = SettingsState.LogoutSucceeded
        } catch (e: Exception) {
            Log.e(TAG, "Cannot logout", e)
            _state.value = SettingsState.LogoutFailed
        }
    }

    /**
     * Deleting the current account.
     * @param context see [android.content.Context]
     */

    @RequiresApi(Build.VERSION_CODES.M)
    fun deleteAccount() = viewModelScope.launch {

        if (!connectivityManager.hasInternet()) {
            _state.value = SettingsState.InternetRequired
            return@launch
        }

        try {
            _state.value = SettingsState.AccountDeletingProgress

            withContext(Dispatchers.IO) {
                userUseCases.deleteUser()
            }
            session.removeToken()

            _state.value = SettingsState.AccountDeletionSucceeded
        } catch (e: Exception) {
            Log.e(TAG, "Cannot delete account", e)
            _state.value = SettingsState.AccountDeletionFailed
        }
    }
}
