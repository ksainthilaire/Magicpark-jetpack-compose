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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
@Preview
fun Counter() {

    var counter = remember { mutableStateOf(0) }


    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {

        CircleButton(
            imageVector = Icons.Default.Add,
            contentDescription = "Minus",
        ){
            counter.value -= 1
        }

        Text(
            text = counter.value.toString(),
            modifier = Modifier.padding(start=10.dp, end=10.dp),
            style = TextStyle(
                fontSize = 15.sp,
                color = Color.Black
            )
        )

        CircleButton(
            imageVector = Icons.Default.Add,
            contentDescription = "Add"
        ){
            counter.value += 1
        }

    }

}