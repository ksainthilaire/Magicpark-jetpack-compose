package com.magicpark.utils.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.magicpark.domain.model.Movie
import com.magicpark.utils.R

@Composable
fun MovieStars(movie: Movie) {
    movie.voteAverage?.let { voteAverage ->
        (0 until 10).forEach { starNumber ->
            if (starNumber < voteAverage.toInt()) Image(
                painterResource(R.drawable.ic_star_filled),
                "",
                Modifier
                    .width(15.dp)
                    .height(15.dp)
            )
            else Image(
                painterResource(R.drawable.ic_star_empty),
                "",
                Modifier
                    .width(15.dp)
                    .height(15.dp)
            )
        }
    }
}