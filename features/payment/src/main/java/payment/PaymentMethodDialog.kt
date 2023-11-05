package com.magicpark.features.payment.payment

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.magicpark.core.MagicparkMaterialTheme
import com.magicpark.domain.enums.PaymentMethod
import com.magicpark.utils.ui.CallbackWithParameter
import com.magicpark.utils.ui.CallbackWithoutParameter

private val PaymentMethod.stringRes: String
    get() = when (this) {
        PaymentMethod.Orange -> "Orange"
        PaymentMethod.PayPal -> "PayPal"
        PaymentMethod.Stripe -> "Stripe"
    }

@Composable
@Preview
private fun PaymentMethodDialog_Preview() {
    PaymentMethodDialog(onClose = { /*TODO*/ }, onSelectedPaymentMethod = {})
}

/**
 * @param onClose The user closes the dialog.
 * @param onSelectedPaymentMethod User chooses to pay with payment method.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentMethodDialog(
    onClose: CallbackWithoutParameter,
    onSelectedPaymentMethod: CallbackWithParameter<PaymentMethod>,
) {
    val context = LocalContext.current

    val paymentMethods = PaymentMethod.values().map { it.stringRes }

    var expanded by remember { mutableStateOf(false) }
    var selectedPaymentMethod by remember { mutableStateOf(paymentMethods[0]) }

    AlertDialog(
        onDismissRequest = { onClose() },
        title = { Text(text = stringResource(com.magicpark.utils.R.string.payment_title)) },
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {
                    TextField(
                        value = selectedPaymentMethod,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        paymentMethods.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item) },
                                onClick = {
                                    selectedPaymentMethod = item
                                    expanded = false
                                    Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSelectedPaymentMethod(PaymentMethod.Orange)//selectedPaymentMethod)
                }
            ) {
                Text(text = stringResource(com.magicpark.utils.R.string.common_button_continue))
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onClose()
                }
            ) {
                Text(text = stringResource(com.magicpark.utils.R.string.common_button_cancel))
            }
        }
    )
}
