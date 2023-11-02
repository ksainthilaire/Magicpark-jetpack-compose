package settings

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magicpark.core.Config
import com.magicpark.domain.model.User
import com.magicpark.domain.usecases.UserUseCases
import contact.ContactViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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
     * User deletion was successful
     */
    object AccountDeletionSucceeded : SettingsState

    /**
     * User deletion failed
     */
    object AccountDeletionFailed : SettingsState
}


class SettingsViewModel : ViewModel() {

    companion object {
        private val TAG = SettingsViewModel::class.java.simpleName
    }

    private val userUseCases: UserUseCases by KoinJavaComponent.inject(UserUseCases::class.java)

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
        val user = userUseCases.getUser()
        _user.value = user
    }

    /**
     * Update user information.
     * @param fullName Full name
     * @param mail User mail
     * @param number Phone number
     */
    fun updateUser(
        fullName: String,
        mail: String,
        number: String,
    ) = viewModelScope.launch {

        userUseCases
            .updateUser(
                mail,
                fullName,
                number,
                null,
                null
            )
    }

    /**
     * Delete the session token associated with the user that is saved in the share preferences.
     * @param context see [android.content.Context]
     */
    private fun clearToken(context: Context) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(
                Config.KEY_SHARED_PREFERENCES,
                Context.MODE_PRIVATE
            )

        sharedPreferences.edit()
            .apply {
                remove(ContactViewModel.KEY_API_TOKEN)
                apply()
            }
    }

    /**
     * Logs out the current user.
     * @param context see [android.content.Context]
     */
    fun logout(context: Context) = viewModelScope.launch {
        try {
            userUseCases.logout()
            clearToken(context)

            _state.value = SettingsState.LogoutSucceeded
        } catch (e: Exception) {
            _state.value = SettingsState.LogoutFailed
        }
    }

    /**
     * Deleting the current account.
     * @param context see [android.content.Context]
     */

    fun deleteAccount(context: Context) = viewModelScope.launch {
        try {
            userUseCases.deleteUser()
            clearToken(context)

            _state.value = SettingsState.AccountDeletionSucceeded
        } catch (e: Exception) {
            _state.value = SettingsState.AccountDeletionFailed
        }
    }
}
