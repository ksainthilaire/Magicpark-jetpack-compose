package io.dvlt.features.movies

import io.dvlt.domain.model.Movie

class State(
    val isLoading: Boolean = true,
    val isInitialized: Boolean = false,
    val movies: List<Movie> = listOf()
)