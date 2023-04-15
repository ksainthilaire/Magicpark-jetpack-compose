package com.magicpark.features.account

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.magicpark.utils.R
import com.magicpark.utils.ui.MovieCard
import com.magicpark.utils.ui.TabDescriptor
import com.magicpark.utils.ui.Tabs

@Composable
fun MoviesScreen(onNavigateToMovie: (id: Int) -> Unit)  {
    val viewModel = remember { AccountViewModel() }

    val state by viewModel.state.observeAsState()



}
