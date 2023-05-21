package com.magicpark.utils.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.magicpark.core.MagicparkTheme
import com.magicpark.utils.R
import kotlinx.coroutines.delay


@Composable
fun SuccessSnackbar(text: String, duration: Long = 2000L) {
    var isVisible by remember { mutableStateOf(true) }

    if (isVisible) {
        Snackbar(
            containerColor = Color.Green,
            action = {
                Button(onClick = {}) {
                    Text(stringResource(id = R.string.common_button_close))
                }
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = text,
                style = TextStyle(color = Color.White)
            )
        }
    }

    LaunchedEffect(key1 = isVisible) {
        delay(duration)
        isVisible = false
    }
}

@Composable
fun ErrorSnackbar(
    text: String,
    onDismiss: () -> Unit,
) {
    Snackbar(
        containerColor = MagicparkTheme.colors.primary,
        action = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(stringResource(id = R.string.common_button_re_try))
            }
        },
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = text,
            style = TextStyle(color = Color.White)
        )
    }

}