package com.magicpark.data.model.request

import com.google.gson.annotations.SerializedName

data class ExportRequest(
    @SerializedName("page")
    val page: Int? = null,

    @SerializedName("items")
    val items: List<Int>? = null
)
