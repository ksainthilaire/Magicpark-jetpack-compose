package io.dvlt.domain.model

import com.google.gson.annotations.SerializedName

data class ProductionCountry(
    /*
        https://www.loc.gov/standards/iso639-2/php/code_list.php
    */
    @SerializedName("iso_3166_1")
    val iso: String? = null,
    val name: String? = null
)