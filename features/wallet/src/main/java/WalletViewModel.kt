package com.magicpark.features.wallet

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magicpark.domain.model.UserTicket
import com.magicpark.domain.usecases.TicketUseCases
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent


class WalletViewModel : ViewModel() {


    private val walletUseCases: TicketUseCases by KoinJavaComponent.inject(TicketUseCases::class.java)

    private val _state: MutableLiveData<WalletState> = MutableLiveData()

    val _toUse: MutableLiveData<List<UserTicket>> = MutableLiveData()
    val toUse: LiveData<List<UserTicket>>
        get() = _toUse

    val _inUse: MutableLiveData<List<UserTicket>> = MutableLiveData()
    val inUse: LiveData<List<UserTicket>>
        get() = _inUse


    val state: LiveData<WalletState>
        get() = _state


    init {
        loadWalletList()
    }

    private fun onWalletItems(wallet: List<UserTicket>) {
        _inUse.postValue(wallet)
        _toUse.postValue(wallet)
    }

    private fun onWalletError(throwable: Throwable) {
        Log.d(TAG, "Wallet error")
    }

    fun loadWalletList() = viewModelScope.launch {
        walletUseCases.getWallet()
            .subscribe(::onWalletItems, ::onWalletError)
    }

    companion object {
        const val TAG = "WalletViewModel"
    }


}
