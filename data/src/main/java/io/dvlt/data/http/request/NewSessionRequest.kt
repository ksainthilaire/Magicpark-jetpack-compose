package io.dvlt.data.http.request

import com.google.gson.annotations.SerializedName

class NewSessionRequest(
    @SerializedName("request_token")
    val requestToken: String
)