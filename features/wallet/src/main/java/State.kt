package com.magicpark.features.moviedetail

import com.magicpark.domain.model.Movie


sealed class WalletState {


    data class LoadTickets(val tickets: List<UserTicket>) : WalletState()
}