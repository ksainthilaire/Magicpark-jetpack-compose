package io.dvlt.data.http.response

import com.google.gson.annotations.SerializedName
import io.dvlt.data.http.base.ErrorResponse
import io.dvlt.domain.model.Movie


class TrendingMoviesResponse(
    val page: Int? = null,

    @SerializedName("results")
    val results: List<Movie>? = null,

    @SerializedName("total_pages")
    val totalPages: Int? = null,

    @SerializedName("total_results")
    val totalResults: Int? = null
) : ErrorResponse()
