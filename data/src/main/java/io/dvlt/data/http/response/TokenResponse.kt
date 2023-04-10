package io.dvlt.data.http.response

import com.google.gson.annotations.SerializedName
import io.dvlt.data.http.base.ErrorResponse

class TokenResponse(
    val success: Boolean? = null,

    @SerializedName("expires_at")
    val expiresAt: String? = null,

    @SerializedName("request_token")
    val requestToken: String? = null
) : ErrorResponse()