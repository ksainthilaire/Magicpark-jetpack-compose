package com.magicpark.main

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.magicpark.core.MagicparkMaterialTheme
import com.magicpark.core.MagicparkTheme
import com.magicpark.domain.model.ShopItem
import com.magicpark.domain.model.UserTicket
import com.magicpark.features.account.UpdateAccountScreen
import com.magicpark.features.login.LoginActivity
import com.magicpark.utils.ui.Session
import com.magicpark.utils.ui.SessionEvent
import com.magicpark.features.payment.payment.PaymentScreen
import com.magicpark.features.shop.Cart
import com.magicpark.features.shop.cart.CartScreen
import com.magicpark.features.shop.shopItem.ShopItemScreen
import com.magicpark.features.shop.shopList.ShopListScreen
import com.magicpark.ui.webview.WebViewScreen
import com.magicpark.utils.R
import contact.ContactScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent
import settings.SettingsScreen
import ticket.TicketScreen
import wallet.WalletScreen


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private val session: com.magicpark.utils.ui.Session by KoinJavaComponent.inject(
        com.magicpark.utils.ui.Session::class.java
    )

    private val cart: Cart by KoinJavaComponent.inject(
        Cart::class.java
    )

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { session.events.collect(::onSessionEvent) }
            }
        }

        setContentView(createView())

        if (!session.isConnected) {
            Log.i(TAG, "The user is not logged in, he is redirected to LoginActivity.")
            return goToLoginActivity()
        }
    }

    private fun createView(
    ): View =
        ComposeView(this@MainActivity)
            .apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    MagicparkMaterialTheme {
                        MainNavigation()
                    }
                }
            }

    @Composable
    private fun MainNavigation() {
        val navController = rememberNavController()



        NavHost(navController, startDestination = Navigation.Shop.path) {


            composable(Navigation.Wallet.path) {


                ConstraintLayout(Modifier.fillMaxSize()) {

                    val (screen, bottomNavigation) = createRefs()

                    Box(
                        Modifier
                            .padding(vertical = 48.dp)
                            .fillMaxSize()
                            .constrainAs(screen) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(bottomNavigation.top)
                                width = Dimension.fillToConstraints
                            }
                    ) {
                        WalletScreen(
                            goToTicket = { ticket ->
                                navController.apply {
                                    currentBackStackEntry
                                        ?.savedStateHandle
                                        ?.set(Navigation.KEY_SHOP_ARG, ticket)

                                    navController.navigate(Navigation.Ticket.path)
                                }
                            }
                        )
                    }


                    BottomNavigation(
                        modifier = Modifier
                            .wrapContentSize()
                            .constrainAs(bottomNavigation) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                                width = Dimension.fillToConstraints
                            },
                        navigations = parentNavigations,
                        currentNavigation = Navigation.Wallet,
                        goTo = {
                            navController.navigate(it.path)
                        })
                }

            }
            composable(Navigation.Ticket.path) {

                val ticket: UserTicket = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get(Navigation.KEY_TICKET_ARG) as UserTicket? ?: UserTicket()

                TicketScreen(ticket = ticket, onBackPressed = navController::popBackStack, download = { /*TODO*/ })
            }

            composable(Navigation.Shop.path) {
                ConstraintLayout(Modifier.fillMaxSize()) {

                    val (screen, bottomNavigation) = createRefs()

                    Box(
                        Modifier
                            .padding(vertical = 48.dp)
                            .fillMaxSize()
                            .constrainAs(screen) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(bottomNavigation.top)
                                width = Dimension.fillToConstraints
                            }
                    ) {
                        ShopListScreen(
                            goToShopItem = { shopItem ->

                                navController.currentBackStackEntry
                                    ?.savedStateHandle?.set(Navigation.KEY_SHOP_ARG, shopItem)

                                navController.navigate(Navigation.ShopItem.path)
                            }
                        )
                    }


                    BottomNavigation(
                        modifier = Modifier
                            .wrapContentSize()
                            .constrainAs(bottomNavigation) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                                width = Dimension.fillToConstraints
                            },
                        navigations = parentNavigations,
                        currentNavigation = Navigation.Shop,
                        goTo = {
                            navController.navigate(it.path)
                        })
                }
            }
            composable(Navigation.ShopItem.path) {

                val shopItem: ShopItem = navController
                    .previousBackStackEntry
                    ?.savedStateHandle
                    ?.get(Navigation.KEY_SHOP_ARG) as ShopItem? ?: ShopItem()

                ShopItemScreen(shopItem = shopItem, onBackPressed = navController::popBackStack)
            }

            composable(Navigation.Cart.path) {
                CartScreen(
                    showPaymentDialog = { /*TODO*/ },
                    onBackPressed = navController::popBackStack
                )
            }

            composable(Navigation.Payment.path) {
                PaymentScreen(
                    onSuccess = { /*TODO*/ },
                    onFailed = { /*TODO*/ },
                    onCanceled = { /*TODO*/ }) {
                }
            }

            composable(Navigation.Settings.path) {

                ConstraintLayout(Modifier.fillMaxSize()) {

                    val (screen, bottomNavigation) = createRefs()

                    Box(
                        Modifier
                            .padding(vertical = 48.dp)
                            .fillMaxSize()
                            .constrainAs(screen) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(bottomNavigation.top)
                                width = Dimension.fillToConstraints
                            }
                    ) {
                      SettingsScreen(
                          onBackPressed = navController::popBackStack,
                          logout = { /*TODO*/ },
                          deleteAccount = { /*TODO*/ },
                          goToAccountSettings = {
                             navController.navigate(Navigation.AccountSettings.path)
                          },
                          goToContact = {
                              navController.navigate(Navigation.AccountSettings.path)
                          },
                          goToPrivacyPolicy = {

                              val privacyPolicyUrl = getString(R.string.privacy_policy_url)

                              navController
                                  .currentBackStackEntry
                                  ?.savedStateHandle
                                  ?.set(Navigation.KEY_WEBVIEW_ARG, privacyPolicyUrl)

                              navController.navigate(Navigation.WebView.path)
                          })
                    }


                    BottomNavigation(
                        modifier = Modifier
                            .wrapContentSize()
                            .constrainAs(bottomNavigation) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                                width = Dimension.fillToConstraints
                            },
                        navigations = parentNavigations,
                        currentNavigation = Navigation.Settings,
                        goTo = {
                            navController.navigate(it.path)
                        })
                }
            }
            
            composable(Navigation.WebView.path) {

                val url: String = navController
                    .previousBackStackEntry
                    ?.savedStateHandle
                    ?.get(Navigation.KEY_WEBVIEW_ARG) ?: ""

                WebViewScreen(onBackPressed = navController::popBackStack, url = url)
            }

            composable(Navigation.AccountSettings.path) {
                UpdateAccountScreen(onBackPressed = navController::popBackStack)
            }
            composable(Navigation.Contact.path) {
                ContactScreen(onBackPressed = navController::popBackStack)
            }
        }

    }


    private fun onSessionEvent(event: com.magicpark.utils.ui.SessionEvent) {
        println("event $event")
        when (event) {
            is com.magicpark.utils.ui.SessionEvent.Disconnected ->
                goToLoginActivity()

            is com.magicpark.utils.ui.SessionEvent.Connected ->
                Unit
        }
    }

    private fun goToLoginActivity() {
        val intent = LoginActivity.intentFor(context = this)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        cart.saveCart()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FAB(goToCart: () -> Unit) {
    AnimatedVisibility(visible = true) {
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigation(
    modifier: Modifier = Modifier,
    navigations: List<Navigation.ParentNavigation>,
    currentNavigation: Navigation,
    goTo: (Navigation) -> Unit,
) {
    BottomAppBar(modifier = modifier.clip(shape = RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp)), containerColor = Color(0xFFFFCA00)) {
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
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(navigation.iconRes),
                                modifier = Modifier
                                    .size(24.dp),
                                contentDescription = null,
                                tint = MagicparkTheme.colors.primary,
                            )

                            Text(
                                text = stringResource(id = navigation.stringRes),
                                fontWeight = FontWeight.SemiBold,
                                style = TextStyle(
                                    color = MagicparkTheme.colors.primary,
                                )
                            )
                        }
                    },
                    icon = {
                    }
                )
            }
    }
}
