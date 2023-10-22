package com.magicpark.features.wallet


import com.magicpark.domain.model.User
import com.magicpark.domain.model.UserTicket


sealed interface WalletState {

    object Loading : WalletState
    
    data class Tickets(
        val inUse: List<UserTicket>,
        val toUse: List<UserTicket>,
    ) : WalletState
}
