package com.magicpark.features.wallet.wallet


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.magicpark.core.MagicparkMaterialTheme
import com.magicpark.domain.model.UserTicket
import com.magicpark.utils.ui.CallbackWithParameter
import com.magicpark.utils.ui.MagicparkContainer
import dagger.hilt.android.AndroidEntryPoint
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


@AndroidEntryPoint
class WalletFragment : Fragment() {

    private val viewModel: WalletViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext())
            .apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    val state by viewModel.state.collectAsState()

                    MagicparkMaterialTheme {
                        WalletScreen(
                            state = state,
                            goToTicket = {},
                        )
                    }
                }
            }
}

@Preview
@Composable
fun WalletFragmentPreview() =
    WalletScreen(
        state = WalletState.Loading,
        goToTicket = {},
    )


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun Ticket(shopItem: UserTicket, callback: (id: Long) -> Unit) = Column {

    Column(Modifier.padding(20.dp)) {


        Column(
            Modifier
                .clip(CircleShape.copy(CornerSize(24.dp)))
                .background(Color.White)
                .fillMaxWidth()
        ) {


            Row(Modifier.padding(10.dp)) {
                Column(Modifier.weight(0.7f)) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        GlideImage(
                            model = shopItem?.imageUrl ?: "",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp)),
                            contentDescription = null
                        )

                        Text(
                            text = shopItem?.name ?: "<name>",
                            modifier = Modifier.padding(start = 10.dp),
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 20.sp
                            )
                        )

                    }

                    Text(
                        text = "",// shopItem?.description ?: "",
                        Modifier.padding(top = 10.dp),
                        style = TextStyle(
                            color = Color.Gray
                        )
                    )

                    Button(
                        modifier = Modifier.padding(top = 10.dp),
                        onClick = {
                            callback(shopItem.id ?: 0L)
                        },
                    ) {
                        Text(text = "Valider le ticket")
                    }
                }


                Divider(
                    Modifier
                        .fillMaxHeight()
                        .padding(end = 10.dp)
                        .width(1.dp)
                        .background(Color.Black)
                )


                Column(Modifier.weight(0.3f)) {

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "1",//shopItem?.quantity?.toString() ?: "1",
                            style = TextStyle(
                                color = Color.Black
                            )
                        )

                        Icon(
                            imageVector = ImageVector.vectorResource(com.magicpark.core.R.drawable.ic_ticket),
                            modifier = Modifier
                                .size(24.dp)
                                .padding(start = 10.dp),
                            contentDescription = "drawable icons",
                            tint = Color.Unspecified
                        )
                    }


                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "1", //shopItem?.quantity?.toString() ?: "1",
                            style = TextStyle(
                                color = Color.Black
                            )
                        )

                        Icon(
                            imageVector = Icons.Default.Person,
                            modifier = Modifier
                                .size(24.dp)
                                .padding(start = 10.dp),
                            contentDescription = "drawable icons",
                            tint = Color.Unspecified
                        )


                    }

                    Divider(
                        Modifier
                            .fillMaxWidth()
                            .background(Color.Black)
                            .height(1.dp)
                    )

                    Text(
                        text = "Expire le 10-04-2023",
                        Modifier.padding(top = 10.dp),
                        style = TextStyle(
                            color = Color.Gray
                        )
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun WalletScreen(
    state: WalletState,
    goToTicket: CallbackWithParameter<Long>,
) {
    var selectedTabIndex by remember { mutableStateOf(0) }

    MagicparkContainer {
        LazyColumn(modifier = Modifier.fillMaxSize()) {

            item {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    TabRow(
                        selectedTabIndex = selectedTabIndex,
                        modifier = Modifier.clip(
                            RoundedCornerShape(
                                topStart = 20.dp,
                                topEnd = 20.dp
                            )
                        )
                    ) {
                        Tab(selectedTabIndex == 0, {
                            selectedTabIndex = 0
                        }) {
                            Text(
                                "À VALIDER",
                                Modifier.padding(10.dp),
                                style = TextStyle(fontSize = 24.sp)
                            )
                        }
                        Tab(selectedTabIndex == 1, {
                            selectedTabIndex = 1
                        }) {
                            Text(
                                "EXPIRÉES",
                                Modifier.padding(10.dp),
                                style = TextStyle(fontSize = 24.sp)
                            )
                        }
                    }
                }

                val wallet = state as? WalletState.Tickets ?: return@item

                if (selectedTabIndex == 0) wallet
                    .toUse.forEach { ticket ->
                        Ticket(ticket, goToTicket)
                    }
                else wallet.inUse.forEach { ticket ->
                    Ticket(ticket, goToTicket)
                }
            }
        }
    }
}
