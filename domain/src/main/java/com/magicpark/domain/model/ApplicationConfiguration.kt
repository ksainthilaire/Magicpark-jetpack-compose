package com.magicpark.domain.model.magicpark

import com.google.gson.annotations.SerializedName


data class ApplicationConfiguration(
    @SerializedName("app_version_name")
    val appVersionName: String? = null,
    @SerializedName("app_url")
    val appUrl: String? = null,
)