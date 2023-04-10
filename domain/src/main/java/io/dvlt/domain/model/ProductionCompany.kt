package io.dvlt.domain.model

import com.google.gson.annotations.SerializedName

data class ProductionCompany(
    val name: String? = null,
    val id: Int? = null,
    @SerializedName("logo_path")
    val logoPath: String? = null,
    @SerializedName("origin_country")
    val originCountry: String? = null
)