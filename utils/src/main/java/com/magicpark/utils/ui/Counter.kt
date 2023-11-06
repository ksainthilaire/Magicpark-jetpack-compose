package com.magicpark.utils.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.magicpark.core.MagicparkTheme

@Composable
fun CircleButton(
    imageVector: ImageVector,
    contentDescription : String = "",
    clickable : Boolean = true,
    click: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .size(30.dp)
            .background(MagicparkTheme.colors.primary.copy(alpha = 0.2f))
            .clickable(
                enabled = clickable,
                onClick = click
            )
    ) {

        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = MagicparkTheme.colors.primary,
            modifier = Modifier
                .size(20.dp)
        )

    }
}


@Composable
fun Counter(
    value: Int,
    removeListener: CallbackWithoutParameter,
    addListener: CallbackWithoutParameter,
) {

    var counter by remember { mutableStateOf(value) }

    Row(
        modifier = Modifier.wrapContentSize(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircleButton(imageVector = Icons.Filled.Remove, contentDescription = "Minus") {
            counter -= 1
            removeListener()
        }

        Text(
            text = counter.coerceAtLeast(1).toString(),
            modifier = Modifier.padding(horizontal = 10.dp),
            style = TextStyle(
                fontSize = 16.sp,
                color = Color.Black
            )
        )

        CircleButton(imageVector = Icons.Default.Add, contentDescription = "Add") {
            counter += 1
            addListener()
        }
    }
}
