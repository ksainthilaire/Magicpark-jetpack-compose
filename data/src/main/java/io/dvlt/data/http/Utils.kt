package io.dvlt.data.http

import io.dvlt.data.http.base.ErrorResponse


fun <T : ErrorResponse> mapHttpError(response: T): T {
    if (!response.isSuccessful()) {
        val error = response as ErrorResponse
        throw Throwable(error.statusMessage)
    }
    return response
}
