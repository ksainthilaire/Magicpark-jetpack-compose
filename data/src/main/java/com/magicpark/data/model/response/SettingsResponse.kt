package com.magicpark.data.model.response

import com.google.gson.annotations.SerializedName

data class SettingsResponse(
    @SerializedName("app_version_name")
    val appVersionName: String? = null,
    @SerializedName("app_url")
    val appUrl: String? = null,
    @SerializedName("app_shop_version")
    val appShopVersion: String? = null
) : com.magicpark.data.model.base.ErrorResponse()