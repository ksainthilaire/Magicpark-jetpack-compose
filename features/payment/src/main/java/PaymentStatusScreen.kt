package com.magicpark.features.payment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController


enum class PaymentStatusEnum(val value: String) {
    SUCCESS("success"),
    FAILED("failed"),
    ERROR("error")
}

private fun getPaymentStatusText(state: String): String {
    return when (state) {
        PaymentStatusEnum.SUCCESS.value -> ""
        PaymentStatusEnum.ERROR.value -> ""
        PaymentStatusEnum.FAILED.value -> ""
        else -> ""
    }
}

private fun getPaymentTransactionText(state: String): String {
    return when (state) {
        PaymentStatusEnum.SUCCESS.value -> ""
        PaymentStatusEnum.ERROR.value -> ""
        PaymentStatusEnum.FAILED.value -> ""
        else -> ""
    }
}

private fun getPaymentButtonText(state: String): String {
    return when (state) {
        PaymentStatusEnum.SUCCESS.value -> ""
        PaymentStatusEnum.ERROR.value -> ""
        PaymentStatusEnum.FAILED.value -> ""
        else -> ""
    }
}

private fun getNavigationByStatus(state: String): String {
    return when (state) {
        PaymentStatusEnum.SUCCESS.value -> "/wallet"
        PaymentStatusEnum.ERROR.value -> "/cart"
        PaymentStatusEnum.FAILED.value -> "/cart"
        else -> "/cart"
    }
}


@Preview
@Composable
fun PaymentStatus(navController: NavController? = null, status: String = "success") {
    

    Column(Modifier.fillMaxSize()) {

        Text(text = getPaymentStatusText(status))
        Text(text = getPaymentTransactionText(status))

        Text(text = stringResource(com.magicpark.utils.R.string.payment_transaction_number))

        Row {
            Text(text = stringResource(com.magicpark.utils.R.string.payment_total_amount))
            Text(text = "50") // Montant total
        }

        Row {
            Text(text = stringResource(com.magicpark.utils.R.string.payment_payed_by))
            Text(text = "50") // Payer avec
        }
        Row {
            Text(text = stringResource(com.magicpark.utils.R.string.payment_transaction_date))
            Text(text = "50") // Date de paiement
        }


        Button(
            onClick = {
                      navController?.navigate(getNavigationByStatus(status))
            },
        ) {
            Text(getPaymentButtonText(status))
        }

    }
}