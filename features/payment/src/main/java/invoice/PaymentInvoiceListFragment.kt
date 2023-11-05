package com.magicpark.features.payment.invoice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import com.magicpark.core.MagicparkMaterialTheme
import com.magicpark.utils.ui.CallbackWithParameter
import dagger.hilt.android.AndroidEntryPoint
import org.koin.androidx.viewmodel.ext.android.viewModel

@AndroidEntryPoint
class PaymentInvoiceListFragment : Fragment() {

    private val viewModel: PaymentInvoiceListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext())
            .apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    MagicparkMaterialTheme {
                        PaymentInvoiceListScreen(goToInvoice = {})
                    }
                }
            }

    /**
     * @param Go to invoice with order [id]
     */
    private fun goToInvoice(id: Long) {

    }
}

@Preview
@Composable
private fun PaymentInvoiceListScreenPreview() {
    PaymentInvoiceListScreen(goToInvoice = {})
}

/**
 * @param goToInvoice callback to display a specific invoice
 */
@Composable
private fun PaymentInvoiceListScreen(goToInvoice: CallbackWithParameter<Long>) {
}
