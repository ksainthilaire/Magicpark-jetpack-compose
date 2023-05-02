package com.magicpark.core

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object MagicparkTheme {
    val colors: ColorScheme
        @Composable
        get() = MaterialTheme.colorScheme.copy(
            primary = Color(0xFFAD061C),
            onPrimary = Color.White,
            primaryContainer = Color(0xFFAD061C),
            onPrimaryContainer = Color.White
        )

    val typography: androidx.compose.material3.Typography
        @Composable
        get() = MaterialTheme.typography

    val shapes: Shapes
        @Composable
        get() = MaterialTheme.shapes


    val buttonGreen = Color(0xFF053807)


    val magicparkBackgroundRed = Color(0xFFAD061C)


    val facebookButtonColor = Color(0xFF3b5998)


    val defaultPadding = 16.dp
}

@Composable
fun MagicparkMaterialTheme(
    content: @Composable() () -> Unit
) {

    MaterialTheme(
        colorScheme = MagicparkTheme.colors,
        typography = MagicparkTheme.typography,
        shapes = MagicparkTheme.shapes,
        content = content
    )
}