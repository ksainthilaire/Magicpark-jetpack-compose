package com.magicpark.ui.menu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.magicpark.core.MagicparkTheme
import com.magicpark.utils.R


private interface IBottomNavigation {
    val label: String
    val icon: Int
    val path: String
}

private data class BottomNavigation(
    override val label: String,
    override val icon: Int,
    override val path: String
) : IBottomNavigation

private val bottomNavigationItemsList: List<BottomNavigation> = listOf(
    BottomNavigation(
        label = "Mes tickets",
        icon = com.magicpark.core.R.drawable.ic_ticket,
        path = "/wallet"
    ),
    BottomNavigation(
        label = "Boutique",
        icon = com.magicpark.core.R.drawable.ic_shop,
        path = "/shop"
    ),
    BottomNavigation(
        label = "Plus",
        icon = com.magicpark.core.R.drawable.ic_search,
        path = "/settings"
    )
)


private fun showFloatingActionButton(route: String): Boolean = when (route) {
    "/home" -> {
        false
    }
    else -> true
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigation(
    navController: NavController? = null,
    content: (@Composable (PaddingValues) -> Unit)
) {


    val backStackEntry = navController?.currentBackStackEntryAsState()
    val route = backStackEntry?.value?.destination?.route ?: "/home"

    Scaffold(
        floatingActionButton = {
            AnimatedVisibility(visible = showFloatingActionButton(route)) {
                FloatingActionButton(onClick = { navController?.navigate("/cart") }) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(com.magicpark.core.R.drawable.ic_cart),
                            modifier = Modifier.size(40.dp),
                            contentDescription = stringResource(R.string.cart_title),
                        )
                        Text(
                            text = stringResource(R.string.cart_title),
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            BottomAppBar(
            ) {


                bottomNavigationItemsList.forEach {

                    val selected = it.path == backStackEntry?.value?.destination?.route

                    NavigationBarItem(
                        selected = selected,
                        onClick = { navController?.navigate(it.path) },
                        label = {
                            Text(
                                text = it.label,
                                fontWeight = FontWeight.SemiBold,
                                style = TextStyle(
                                    color = MagicparkTheme.colors.primary
                                )
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(it.icon),
                                contentDescription = it.label,
                                tint = MagicparkTheme.colors.primary
                            )
                        }
                    )

                }

            }
        }, content = content
    )
}