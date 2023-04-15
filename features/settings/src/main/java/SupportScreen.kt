package com.magicpark.features.settings

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.magicpark.core.Config
import com.magicpark.domain.model.Movie
import com.magicpark.utils.R
import com.magicpark.utils.ui.MovieStars
import java.util.*


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SupportScreen(movieId: Int, onBackPressed: () -> Unit) = Column {
    val viewModePl = remember { SupportViewModel() }

    val movie =  Movie()

    val imageUrl = Uri.parse(Config.assetsUrl)
        .buildUpon().appendPath(movie.posterPath).build().toString()

    LazyColumn {


        item {

            Column(
                modifier = Modifier
                    .padding(start = 15.dp, top = 15.dp)
            ) {
                Image(
                    painterResource(R.drawable.ic_arrow_back),
                    "arrow back",
                    Modifier
                        .width(30.dp)
                        .height(30.dp)
                        .clickable { onBackPressed() }
                )
            }


            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GlideImage(
                    model = imageUrl,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(200.dp)
                        .height(400.dp)
                        .clip(RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp)),
                    contentDescription = ""
                )

                Spacer(modifier = Modifier.height(10.dp))


                movie.title?.let {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = it,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
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

                movie.voteCount?.let { Text(text = "$it votes") }
                movie.releaseDate?.let { Text(text = "Date de sortie: $it") }


                Spacer(modifier = Modifier.height(5.dp))


                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "À propos du film",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                movie.overview?.let { Text(textAlign = TextAlign.Justify, text = it) }
            }
        }

    }
}
