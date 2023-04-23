package com.magicpark.features.wallet

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.magicpark.core.Config
import com.magicpark.core.MagicparkTheme
import com.magicpark.domain.model.Movie
import com.magicpark.features.wallet.WalletViewModel
import com.magicpark.utils.R
import com.magicpark.utils.ui.MovieStars
import java.util.*


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
@Preview
fun WalletScreen(navController: NavController? = null)  {


    LazyColumn(Modifier.fillMaxSize()){

        item {


        }
    }

}