package com.magicpark.data.model.response

import com.google.gson.annotations.SerializedName

data class StatisticsResponse(
    @SerializedName("registered_users")
    var registeredUsers: Int? = null,
    var visitors: Int? = null,
    @SerializedName("completed_orders")
    var completedOrders: Int? = null,

    @SerializedName("tickets_generated")
    var ticketsGenerated: Int? = null,

    var money: String? = null,
    var names: List<String>? = null,
    var values: List<String>? = null
)