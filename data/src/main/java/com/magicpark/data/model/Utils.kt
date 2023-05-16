package com.magicpark.data.http

import com.magicpark.data.model.base.ErrorResponse


fun <T : ErrorResponse> mapHttpError(response: T): T {
    if (!response.isSuccessful()) {
        val error = response as ErrorResponse
        throw Throwable(error.statusMessage)
    }
    return response
}
