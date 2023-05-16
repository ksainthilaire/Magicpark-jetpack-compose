package com.magicpark.data.model.response

import com.google.gson.annotations.SerializedName


data class ErrorResponse(
    @SerializedName("status_message")
    val statusMessage: String? = null,
    @SerializedName("status_code")
    val statusCode: String? = null 
)
