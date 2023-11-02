package com.magicpark.features.payment.invoice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import com.magicpark.features.payment.PaymentStatusEnum
import com.magicpark.features.payment.PaymentStatusEnum.*
import com.magicpark.features.payment.statusText
import com.magicpark.utils.R
import com.magicpark.utils.ui.CallbackWithParameter
import com.magicpark.utils.ui.CallbackWithoutParameter
import dagger.hilt.android.AndroidEntryPoint
import org.koin.androidx.viewmodel.ext.android.viewModel

@AndroidEntryPoint
class PaymentInvoiceFragment : Fragment() {

    private val viewModel: PaymentInvoiceViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext())
            .apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {

                    PaymentInvoiceScreen(
                        state = SUCCESS,
                        download = {},
                        tryAgain = {},
                    )
                }
            }

    private fun generatePdf() {}
}

@Preview
@Composable
private fun PaymentInvoiceScreenPreview() {
    PaymentInvoiceScreen(state = SUCCESS, download = {}, tryAgain = {})
}

/**
 * @param download The user wants to download an invoice for their payment.
 * @param tryAgain The user wants to try to pay again.
 */
@Composable
private fun PaymentInvoiceScreen(
    state: PaymentStatusEnum,
    download: CallbackWithParameter<Long>,
    tryAgain: CallbackWithoutParameter?,
) {

    Column(Modifier.fillMaxSize()) {

        Text(text = stringResource(id = state.statusText))
        Text(text = stringResource(id = state.statusText))

        Text(text = stringResource(R.string.payment_transaction_number))

        Row {
            Text(text = stringResource(R.string.payment_total_amount))
            Text(text = "50") // Montant total
        }

        Row {
            Text(text = stringResource(R.string.payment_payed_by))
            Text(text = "50") // Payer avec
        }
        Row {
            Text(text = stringResource(R.string.payment_transaction_date))
            Text(text = "50") // Date de paiement
        }


        Button(
            onClick = {
               // navController?.navigate(s)
            },
        ) {
           // Text(getPaymentButtonText(status))
        }

    }
}
