package com.magicpark.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magicpark.domain.model.User
import com.magicpark.domain.usecases.SupportUseCases
import com.magicpark.domain.usecases.UserUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent


class SettingsViewModel : ViewModel() {

    private val supportUseCases: SupportUseCases by KoinJavaComponent.inject(SupportUseCases::class.java)
    private val userUseCases: UserUseCases by KoinJavaComponent.inject(UserUseCases::class.java)


    private val _user: MutableStateFlow<User> = MutableStateFlow(User())
    val user: StateFlow<User> = _user

    private val _state: MutableStateFlow<SettingsState> = MutableStateFlow(SettingsState.Loading)
    val state: StateFlow<SettingsState> =_state

    private fun loadUser() = viewModelScope.launch {
        val user = userUseCases.getUser()
        _user.value = user
    }

    fun sendHelpRequest(text: String) = viewModelScope.launch {
        val isSuccessful = supportUseCases
            .help(text)

        _state.value = SettingsState.HelpRequestSent
    }

    suspend fun logout() = viewModelScope.launch {
        userUseCases
            .logout()

        _state.value = SettingsState.LogoutSucceeded
    }


    fun updateUser(
        fullName: String,
        mail: String,
        password: String,
        passwordConfirmation: String,
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

    companion object {
        const val TAG = "SettingsViewModel"
    }

}
