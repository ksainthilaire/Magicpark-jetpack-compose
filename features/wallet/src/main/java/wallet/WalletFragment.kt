package wallet

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.magicpark.domain.model.UserTicket
import com.magicpark.domain.model.isExpired
import com.magicpark.utils.R
import com.magicpark.utils.ui.CallbackWithParameter
import com.magicpark.utils.ui.InternetRequiredDialog
import com.magicpark.utils.ui.LoadingScreen
import org.koin.androidx.compose.getViewModel
import java.util.*

enum class WalletCategory(val stringRes: Int) {
    //IN_USE(R.string.wallet_in_use),
    TO_USE(R.string.wallet_to_use),
    EXPIRED(R.string.wallet_expired),
}

/**
 * @param ticket User ticket
 * @param goToTicket listener, go to ticket
 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun Ticket(
    ticket: UserTicket,
    goToTicket: CallbackWithParameter<UserTicket>
) {
    Column(
        Modifier
            .padding(24.dp)
            .clip(CircleShape.copy(CornerSize(24.dp)))
            .background(Color.White)
            .fillMaxWidth(),
    ) {

        Row(modifier = Modifier.padding(24.dp)) {
            Column(modifier = Modifier.weight(0.7f)) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    GlideImage(
                        model = ticket.imageUrl ?: "",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentDescription = null,
                    )

                    Text(
                        text = ticket.name ?: "",
                        modifier = Modifier.padding(start = 12.dp),
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 20.sp
                        )
                    )
                }

                Text(
                    text = "",
                    Modifier.padding(top = 10.dp),
                    style = TextStyle(
                        color = Color.Gray,
                    )
                )

                Button(
                    modifier = Modifier.padding(top = 10.dp),
                    onClick = {
                        goToTicket(ticket)
                    },
                    enabled = !ticket.isExpired()
                ) {
                    Text(text = "Valider le ticket")
                }
            }

            Divider(
                Modifier
                    .fillMaxHeight()
                    .padding(end = 12.dp)
                    .width(1.dp)
                    .background(Color.Black)
            )

            Column(modifier = Modifier.weight(0.3f)) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "1",
                        style = TextStyle(color = Color.Black)
                    )

                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_ticket),
                        modifier = Modifier
                            .size(24.dp)
                            .padding(start = 10.dp),
                        contentDescription = null,
                        tint = Color.Unspecified,
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "1",
                        style = TextStyle(
                            color = Color.Black,
                        ),
                    )

                    Icon(
                        imageVector = Icons.Default.Person,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(start = 10.dp),
                        contentDescription = null,
                        tint = Color.Unspecified,
                    )
                }

                if (ticket.expiredAt != null) {
                    Divider(
                        Modifier
                            .fillMaxWidth()
                            .background(Color.Black)
                            .height(1.dp)
                    )


                    Text(
                        text = "Expire le ${ticket.expiredAt}",
                        Modifier.padding(top = 12.dp),
                        style = TextStyle(
                            color = Color.Gray
                        )
                    )
                }
            }
        }
    }
}

/**
 * @param goToTicket
 */
@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun WalletScreen(
    onBackPressed: (() -> Unit),
    goToTicket: CallbackWithParameter<UserTicket>,
) {

    val viewModel: WalletViewModel = getViewModel()
    val state by viewModel.state.collectAsState()

    var currentCategory by remember { mutableStateOf(WalletCategory.TO_USE) }

    when (val state = state) {

        is WalletState.WalletEmpty -> WalletEmptyScreen()

        is WalletState.Loading -> LoadingScreen()

        is WalletState.Tickets -> {


            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        TabRow(
                            selectedTabIndex = currentCategory.ordinal,
                            modifier = Modifier.clip(
                                RoundedCornerShape(
                                    topStart = 20.dp,
                                    topEnd = 20.dp
                                )
                            )
                        ) {
                            WalletCategory.values()
                                .map { category ->
                                    Tab(
                                        selected = (category.ordinal == currentCategory.ordinal),
                                        onClick = { currentCategory = category }
                                    ) {
                                        val categoryName =
                                            stringResource(id = category.stringRes)

                                        Text(
                                            modifier = Modifier.padding(12.dp),
                                            style = TextStyle(fontSize = 24.sp),
                                            text = categoryName
                                        )
                                    }
                                }
                        }
                    }

                    val tickets = when (currentCategory) {
                        //WalletCategory.IN_USE -> state.inUse
                        WalletCategory.TO_USE -> state.toUse
                        WalletCategory.EXPIRED -> state.expired
                    }

                    tickets.forEach { ticket -> Ticket(ticket, goToTicket) }
                }
            }
        }

        is WalletState.InternetRequired -> {
            Box(Modifier.fillMaxSize()) {
                InternetRequiredDialog(tryAgain = viewModel::checkWallet, onCancel = onBackPressed)
            }
        }

    }
}
