package com.magicpark.features.shop

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.magicpark.utils.R
import com.magicpark.utils.ui.MovieCard
import com.magicpark.utils.ui.TabDescriptor
import com.magicpark.utils.ui.Tabs

@Composable
fun MoviesScreen(onNavigateToMovie: (id: Int) -> Unit)  {
    val viewModel = remember { ShopViewModel() }

    val state by viewModel.state.observeAsState()


    var tabs by remember {
        mutableStateOf(
            listOf(
                TabDescriptor(
                    title = "Films tendances",
                    drawableId = R.drawable.ic_trending_up,
                    onClick = viewModel::getTrending
                ), TabDescriptor(
                    title = "Films les mieux notés",
                    drawableId = R.drawable.ic_star,
                    onClick = viewModel::getTopRated
                )
            )
        )
    }

    viewModel.getTrending()


    Column {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(Modifier.width(IntrinsicSize.Max)) {
                Tabs(tabs) { tab ->
                    tab.onClick()
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))


        LazyColumn {

            item {

                Column {
                    state?.movies?.forEach { movie ->
                        MovieCard(movie, { onNavigateToMovie(it) })
                    }
                }
            }
        }
    }
}

