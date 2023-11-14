package com.magicpark.utils.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SignalCellularConnectedNoInternet0Bar
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.magicpark.utils.R
@Preview
@Composable
fun InternetRequiredDialog(
    tryAgain: (() -> Unit)? = null,
    onCancel: (() -> Unit)? = null,
) {
    AlertDialog(
        icon = {
            Icon(
                imageVector = Icons.Filled.SignalCellularConnectedNoInternet0Bar,
                contentDescription = null,
            )
        },
        title = {
            Text(text = stringResource(id = R.string.internet_required_title), textAlign = TextAlign.Center)
        },
        text = {
            Text(modifier = Modifier.padding(horizontal = 12.dp), text = stringResource(id = R.string.internet_required_text), textAlign = TextAlign.Justify)
        },
        onDismissRequest = {
                           onCancel?.invoke()
        },
        confirmButton = {
            Button(
                onClick = { tryAgain?.invoke() }
            ) { Text(text = stringResource(id = R.string.common_button_re_try)) }
        },
        dismissButton = {
            Button(
                modifier = Modifier
                    .wrapContentSize(),
                onClick = { onCancel?.invoke() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
            ) { Text(text = stringResource(id = R.string.common_button_cancel)) }
        }
    )
}
