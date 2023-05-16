package com.magicpark.data.http.response

import com.google.gson.annotations.SerializedName
import com.magicpark.data.http.base.ErrorResponse



class TrendingMoviesResponse(
    val page: Int? = null,

    @SerializedName("results")
    val results: List<Movie>? = null,

    @SerializedName("total_pages")
    val totalPages: Int? = null,

    @SerializedName("total_results")
    val totalResults: Int? = null
) : ErrorResponse()
