package com.magicpark.data.model.request.authentification

import com.google.gson.annotations.SerializedName



data class UpdateUserRequest(
    val mail: String? = null,

    @SerializedName("full_name")
    val fullName: String? = null,

    @SerializedName("phone_number")
    val phoneNumber: String? = null,

    @SerializedName("avatar_url")
    val avatarUrl: String? = null,

    val country: String? = null
) 