package com.magicpark.utils.ui


import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.magicpark.core.Config
import com.magicpark.domain.model.Movie

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MovieCard(
    movie: Movie,
    onNavigateToMovie: (id: Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .padding(10.dp, 2.dp, 2.dp, 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        onClick = {
            movie.id?.let { onNavigateToMovie(it) }
        }
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {


            val imageUrl = Uri.parse(Config.assetsUrl)
                .buildUpon().appendPath(movie.posterPath).build().toString()

            GlideImage(
                model = imageUrl,
                contentScale = ContentScale.Fit,
                modifier = Modifier.clip(RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp)),
                contentDescription = ""
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp, 0.dp, 0.dp, 0.dp)
            ) {
                movie.title?.let {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = it
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MovieStars(movie = movie)
                    Text(
                        text = movie.voteAverage.toString(),
                        modifier = Modifier.alignByBaseline()
                    )
                }
            }
        }
    }
}
