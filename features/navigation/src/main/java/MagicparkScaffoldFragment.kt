package com.magicpark.core.base

import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.magicpark.utils.R
import component.MagicparkScaffold
import component.Navigation
import dagger.hilt.android.AndroidEntryPoint

private val navigations = listOf(
    Navigation(
        iconRes =
            R.drawable.ic_ticket,
        stringRes =
            R.string.navigation_item_wallet,
    ),


   Navigation(
       iconRes =
            R.drawable.ic_shop,
       stringRes =
            R.string.navigation_item_shop,
       cart =
            true,
   ),

    Navigation(
        iconRes =
            R.drawable.ic_search,

        stringRes =
            R.string.navigation_item_plus,
    )
)

@AndroidEntryPoint
open class MagicparkScaffoldFragment : Fragment() {

    private val navController by lazy {
        findNavController()
    }

    private val Navigation.destinationId: Int
        get() {
            val stringName = resources.getString(stringRes)

            val navDestination = navController.graph.find {
                    navDestination ->
                stringName == navDestination.label
            }
            return requireNotNull(navDestination?.id) { "id cannot be null" }

        }

    private val currentNavigation: Navigation
        get() {
            val route = navController.currentDestination

            return navigations.find { navigation ->
                val stringName = resources.getString(navigation.stringRes)
                stringName == route?.label
            } ?: navigations.first()
        }

    fun compose(content: @Composable () -> Unit): View =
        ComposeView(requireContext())
            .apply {
                setViewCompositionStrategy(
                    ViewCompositionStrategy
                    .DisposeOnViewTreeLifecycleDestroyed
                )
                setContent {

                    MagicparkScaffold(
                        navigations = navigations,
                        currentNavigation = currentNavigation,
                        goTo = this@MagicparkScaffoldFragment::goTo,
                        goToCart = this@MagicparkScaffoldFragment::goToCart,
                    ) {
                        content()
                    }
                }
            }

    private fun goTo(navigation: Navigation) {
        navController.navigate(navigation.destinationId)
    }

    private fun goToCart() {
        //navController.navigate(R
    }
}
