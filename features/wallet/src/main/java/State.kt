package com.magicpark.features.wallet


import com.magicpark.domain.model.UserTicket


sealed class WalletState {


    data class LoadTickets(val tickets: List<UserTicket>) : WalletState()
}