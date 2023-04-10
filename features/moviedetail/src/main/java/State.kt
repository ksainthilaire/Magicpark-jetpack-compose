package io.dvlt.features.moviedetail

import io.dvlt.domain.model.Movie

class State(
    val isLoading: Boolean = true,
    val movie: Movie? = null
)