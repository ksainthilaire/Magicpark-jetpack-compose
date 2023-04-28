package com.magicpark.features.settings

import com.magicpark.domain.model.Movie

class State(
    val isLoading: Boolean = true,
    val movie: Movie? = null
)
