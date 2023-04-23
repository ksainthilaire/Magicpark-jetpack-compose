package com.magicpark.features.shop

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MoviesScreen(onNavigateToMovie: (id: Int) -> Unit)  {
    val viewModel = remember { ShopViewModel() }

    val state by viewModel.state.observeAsState()




    Column {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {

        }

        Spacer(modifier = Modifier.height(20.dp))


    }
}

