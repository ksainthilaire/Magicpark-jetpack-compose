package io.dvlt.utils.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

data class TabDescriptor(
    val title: String,
    @DrawableRes val drawableId: Int,
    val onClick: () -> Unit
)


@Composable
fun Tabs(tabs: List<TabDescriptor>, onClick: (tab: TabDescriptor) -> Unit) {

    var selected = remember { mutableStateOf(tabs.first()) }

    Row(
        Modifier
            .padding(2.dp)
            .height(100.dp)
            .fillMaxWidth()
    ) {

        tabs.forEach { tab ->

            val tabModifier = Modifier.weight(1f, true)
            val isSelected = selected.value == tab
            Tab(
                isSelected,
                {
                    selected.value = tab
                    onClick(tab)
                },
                modifier = if (isSelected) tabModifier.background(
                    color = Color.Black,
                    shape = RoundedCornerShape(8.dp)
                ) else tabModifier,
                selectedContentColor = Color.Gray,
                unselectedContentColor = Color.Black
            ) {
                Column(
                    Modifier
                        .padding(10.dp)
                        .fillMaxHeight()
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(tab.drawableId),
                        contentDescription = ""
                    )
                    Text(
                        text = tab.title,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}