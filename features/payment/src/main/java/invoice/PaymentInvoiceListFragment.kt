package com.magicpark.features.payment.invoice

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.magicpark.core.MagicparkTheme
import com.magicpark.domain.model.Invoice
import com.magicpark.utils.R
import com.magicpark.utils.ui.CallbackWithParameter
import com.magicpark.utils.ui.CallbackWithoutParameter
import com.magicpark.utils.ui.LoadingScreen
import org.koin.androidx.compose.getViewModel

@Preview
@Composable
private fun PaymentInvoiceListScreenPreview() {
    PaymentInvoiceListScreen(onBackPressed = {}, goToInvoice = {})
}

/**
 * @param onBackPressed listener go back
 * @param goToInvoice callback to display a specific invoice
 */
@Composable
fun PaymentInvoiceListScreen(
    onBackPressed: CallbackWithoutParameter,
    goToInvoice: CallbackWithParameter<Invoice>
) {
    val viewModel: PaymentInvoiceListViewModel = getViewModel()
    val state by viewModel.state.collectAsState()

    when (state) {

        is PaymentInvoiceListState.Loading -> LoadingScreen()

        is PaymentInvoiceListState.InvoiceList -> {
            val uiState = state as PaymentInvoiceListState.InvoiceList

            Column(Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.ic_back),
                    modifier = Modifier
                        .padding(24.dp)
                        .size(50.dp)
                        .clickable {
                            onBackPressed()
                        },
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MagicparkTheme.colors.primary)
                )


                LazyColumn(Modifier.fillMaxSize()) {


                    uiState.list.map { invoice ->
                        item {

                            Row(
                               modifier =  Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Column(modifier = Modifier
                                    .clickable { goToInvoice(invoice) }
                                    .padding(vertical = 16.dp, horizontal = 8.dp)
                                    .weight(1f)
                                ) {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = invoice.description ?: "",
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = MagicparkTheme.colors.primary
                                    )
                                    Text(
                                        text = invoice.amount ?: "",
                                        style = TextStyle(
                                            fontSize = 12.sp,
                                        ),
                                    )
                                    Text(
                                        text = invoice.paymentMethod.toString(),
                                        style = TextStyle(
                                            fontSize = 12.sp,
                                        ),
                                    )
                                }

                                Icon(
                                    modifier = Modifier
                                        .padding(end = 12.dp)
                                        .size(16.dp),
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null,
                                    tint = MagicparkTheme.colors.primary,
                                )
                            }
                            Divider()
                        }
                    }

                }
            }
        }
    }
}

