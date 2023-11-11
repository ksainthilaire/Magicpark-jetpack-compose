package component

import android.view.LayoutInflater
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentContainerView
import com.magicpark.core.MagicparkTheme.colors
import com.magicpark.utils.R
import com.magicpark.utils.ui.CallbackWithParameter
import com.magicpark.utils.ui.CallbackWithoutParameter

class Navigation(
    /**
     * Icon that can be displayed in navigation
     */
    val iconRes: Int,

    /**
     * Text to display for this navigation
     */
    val stringRes: Int,

    /**
     * If true, then the basket is displayed when you are positioned on this navigation.
     */
    val cart: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MagicparkScaffold(
    navigations: List<Navigation>,
    currentNavigation: Navigation,
    goTo: CallbackWithParameter<Navigation>,
    goToCart: CallbackWithoutParameter,
    content: (@Composable (PaddingValues) -> Unit)
) {

    Scaffold(
        floatingActionButton = {
            AnimatedVisibility(visible = currentNavigation.cart) {
                FloatingActionButton(onClick = { goToCart() }) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_cart),
                            modifier = Modifier.size(48.dp),
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
            BottomAppBar(containerColor = Color(0xFFFFCA00)) {
              navigations
                    .forEach { navigation ->

                    NavigationBarItem(
                        selected = navigation == currentNavigation,
                        colors = NavigationBarItemDefaults
                            .colors(
                                indicatorColor = Color.Yellow
                            ),
                        onClick = { goTo(navigation) },
                        label = {
                            Text(
                                text = stringResource(id = navigation.stringRes),
                                fontWeight = FontWeight.SemiBold,
                                style = TextStyle(
                                    color = colors.primary,
                                )
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(navigation.iconRes),
                                modifier = Modifier.size(32.dp),
                                contentDescription = null,
                                tint = colors.primary,
                            )
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {

        }
    }
}
