package com.magicpark.utils.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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
            .size(40.dp)
            .background(Color.Green.copy(alpha = 0.1f))
            .clickable(
                enabled = clickable,
                onClick = click
            )
    ) {

        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = Color.Green,
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

        }

        Spacer(modifier = Modifier.size(16.dp))

        CircleButton(
            imageVector = Icons.Default.Add,
            contentDescription = "Add"
        ){

        }

    }

}