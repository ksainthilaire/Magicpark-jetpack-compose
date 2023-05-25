package com.magicpark.features.settings


import android.content.res.Resources
import android.util.Log
import androidx.compose.ui.text.font.FontVariation
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magicpark.domain.model.Settings
import com.magicpark.domain.model.User
import com.magicpark.domain.usecases.SettingsUseCases
import com.magicpark.domain.usecases.SupportUseCases
import com.magicpark.domain.usecases.UserUseCases
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent


class SettingsViewModel : ViewModel() {

    private val supportUseCases: SupportUseCases by KoinJavaComponent.inject(SupportUseCases::class.java)
    private val userUseCases: UserUseCases by KoinJavaComponent.inject(UserUseCases::class.java)


    private val _state: MutableLiveData<SettingsState> = MutableLiveData()
    val _user: MutableLiveData<User> = MutableLiveData()

    val user: LiveData<User>
        get() = _user

    val state: LiveData<SettingsState>
        get() = _state

    init {
        loadUser()
    }

    private fun onSettingsError(throwable: Throwable) {
        Log.d(TAG, "Settings error")
    }

    private fun onUserError(throwable: Throwable) {
        Log.d(TAG, "User error")
    }

    private fun onUserLoaded(user: User) {
        _user.postValue(user)
    }

    private fun onUserUpdated() {
        Log.d(TAG, "User updated")
    }

    fun loadUser() {
        userUseCases.getUser()
            .subscribe(::onUserLoaded, ::onUserError)
    }

    private fun onHelpSucceeded() {
        _state.postValue(SettingsState.HelpRequestSent)
    }

    private fun onHelpError(throwable: Throwable) {
        _state.postValue(SettingsState.HelpRequestError(throwable.message))
    }

    fun help(message: String) {
        supportUseCases.help(message)
            .subscribe(::onHelpSucceeded, ::onHelpError)
    }

    private fun onLogoutSucceeded() {
        _state.postValue(SettingsState.LogoutSucceeded)
    }

    private fun onLogoutError(throwable: Throwable) {
        Log.d(TAG, "Logout error ${throwable.message}")
    }

    fun logout() {
        userUseCases.logout()
            .subscribe(::onLogoutSucceeded, ::onLogoutError)
    }

    fun updateUser(
        fullName: String, mail: String, password: String,
        passwordConfirmation: String, number: String
    ) {

        userUseCases.updateUser(mail, fullName, number, null, null)
            .subscribe(::onUserUpdated, ::onUserError)
    }

    companion object {
        const val TAG = "SettingsViewModel"
    }

}