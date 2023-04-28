package com.magicpark.data.http.base

import com.google.gson.annotations.SerializedName

open class ErrorResponse : Throwable() {
    // HTTP status message
    @SerializedName("status_message")
    val statusMessage: String? = null

    // HTTP status code
    @SerializedName("status_code")
    val statusCode: Int? = null

    open fun isSuccessful(): Boolean {
        return (statusMessage == null && statusCode == null)
    }
}

fun ErrorResponse.getThrowable(): Throwable {
    return Throwable(statusMessage)
}