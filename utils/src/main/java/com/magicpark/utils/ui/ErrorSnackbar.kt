package com.magicpark.utils.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay


@Preview
@Composable
private fun ToastPreview() {
    MaterialTheme {
        Box {
            Toast(
                text = "This is a toast message",
                duration = MagicparkToast.Duration.LONG,
                onClick = {},
                endContent = {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null,
                    )
                }
            )
        }
    }
}

/**
 * Custom [Toast] based on Devialet style.
 *
 * @param text the text to display
 * @param duration Display duration
 * @param backgroundColor Background color
 * @param endContent slot for end content [Composable]
 * @param onClick the click listener
 */
@Composable
fun BoxScope.Toast(
    modifier: Modifier = Modifier
        .zIndex(3f)
        .padding(top = 64.dp)
        .fillMaxWidth(),
    text: String,
    duration: MagicparkToast.Duration = MagicparkToast.Duration.SHORT,
    backgroundColor: Color = MaterialTheme.colorScheme.error,
    endContent: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    val transitionState = remember { MutableTransitionState(initialState = false) }



    AnimatedVisibility(
        visibleState = transitionState
            .apply { targetState = true },
        modifier = modifier,
        enter = fadeIn(animationSpec = tween(durationMillis = 800), initialAlpha = 0.2f),
        exit = fadeOut(animationSpec = tween(durationMillis = 500)),
    ) {
        println("Recomposition transitionState ${transitionState.currentState}")
        Box(
            Modifier
                .padding(horizontal = MagicparkToast.toastHorizontalPadding)
                .wrapContentSize()
                .clickable {
                    onClick?.invoke()
                    transitionState.targetState = false
                }
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(8.dp),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Row(
                Modifier
                    .padding(MagicparkToast.contentPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {

                val hasEndContent = endContent != null
                val endContentModifier = Modifier
                    .size(if (hasEndContent) MagicparkToast.startEndContentSize else 0.dp)

                Text(
                    text = text.uppercase(),
                    modifier = Modifier.padding(horizontal = if (hasEndContent) 6.dp else 0.dp),
                    maxLines = 20,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center,
                )

                Box(endContentModifier) {
                    endContent?.invoke()
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        delay(duration.length)
        transitionState.targetState = false
    }
}


object MagicparkToast {

    internal val toastHorizontalPadding = 24.dp

    internal val startEndContentSize = 24.dp
    internal val toastHeight = 48.dp
    internal val contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)

    enum class Duration(val length: Long) {
        SHORT(2000),
        LONG(3500),
    }
}
