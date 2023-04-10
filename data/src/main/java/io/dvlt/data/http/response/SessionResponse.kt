package io.dvlt.data.http.response

import com.google.gson.annotations.SerializedName
import io.dvlt.data.http.base.ErrorResponse


class SessionResponse(
    val success: Boolean? = null,

    @SerializedName("session_id")
    val sessionId: String? = null
) : ErrorResponse()