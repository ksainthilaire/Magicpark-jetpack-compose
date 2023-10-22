package com.magicpark.features.account

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class AccountViewModel : ViewModel() {

    protected val _state: MutableStateFlow<AccountState> = MutableStateFlow(AccountState.Idle)
    val state: StateFlow<AccountState>
        get() = _state

    private fun onError(t: Throwable) {
        t.printStackTrace()
    }
    
}
