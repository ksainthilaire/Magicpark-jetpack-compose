package com.magicpark.utils.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

private enum class Colors(

)

@Composable
fun MagicparkTheme(content: @Composable CallbackWithoutParameter) {


    val colorScheme = MaterialTheme.colorScheme.copy(

    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes,
    ) {
        content()
    }
}
