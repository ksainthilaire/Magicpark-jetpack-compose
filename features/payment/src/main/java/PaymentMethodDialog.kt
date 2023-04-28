package com.magicpark.features.payment

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


interface PaymentMethodDialogListener {
    fun onClose()
    fun onSelectedPaymentMethod()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun PaymentMethodDialog(listener: PaymentMethodDialogListener? = null) {

    val context = LocalContext.current
    val paymentMethods = arrayOf("Orange Money", "PayPal", "PaySafeCard")
    var expanded by remember { mutableStateOf(false) }
    var selectedPaymentMethod by remember { mutableStateOf(paymentMethods[0]) }

    AlertDialog(
        onDismissRequest = {
             listener?.onClose()
        },
        title = {
            Text(text = stringResource(com.magicpark.utils.R.string.payment_title))
        },
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
                     //   trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
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
                    listener?.onClose()
                }) {
                Text(text = stringResource(com.magicpark.utils.R.string.common_button_continue))
            }
        },
        dismissButton = {
            Button(

                onClick = {
                    listener?.onClose()
                }) {
                Text(text = stringResource(com.magicpark.utils.R.string.common_button_cancel))
            }
        }
    )
}