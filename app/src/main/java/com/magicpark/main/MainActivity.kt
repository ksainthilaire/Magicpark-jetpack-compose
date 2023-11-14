package com.magicpark.main

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.magicpark.domain.enums.PaymentMethod
import com.magicpark.domain.model.ShopItem
import com.magicpark.domain.model.UserTicket
import com.magicpark.features.account.AccountEditPasswordScreen
import com.magicpark.features.account.UpdateAccountScreen
import com.magicpark.features.login.LoginActivity
import com.magicpark.features.payment.PaymentStatusEnum
import com.magicpark.features.payment.invoice.PaymentInvoiceListScreen
import com.magicpark.features.payment.invoice.PaymentInvoiceScreen
import com.magicpark.features.payment.payment.PaymentScreen
import com.magicpark.features.shop.cart.CartScreen
import com.magicpark.features.shop.shopItem.ShopItemScreen
import com.magicpark.features.shop.shopList.ShopListScreen
import com.magicpark.ui.webview.WebViewScreen
import com.magicpark.utils.R
import com.magicpark.utils.ui.Cart
import com.magicpark.utils.ui.CartState
import com.magicpark.utils.ui.Session
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

    private val session: Session by KoinJavaComponent.inject(
        Session::class.java
    )

    private val cart: Cart by KoinJavaComponent.inject(
        Cart::class.java
    )

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            createView()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { session.events.collect(::onSessionEvent) }
            }
        }

        if (!session.isConnected) {
            Log.i(TAG, "The user is not logged in, he is redirected to LoginActivity.")
            return goToLoginActivity()
        }

        createView()
    }


    private fun createView(
    ) = setContentView(
        ComposeView(this@MainActivity)
            .apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    MagicparkMaterialTheme {
                        MainNavigation()
                    }
                }
            }
    )

    @Composable
    private fun MainNavigation() {
        val navController = rememberNavController()



        NavHost(navController, startDestination = Navigation.Shop.path) {


            composable(Navigation.Wallet.path) {


                ConstraintLayout(Modifier.fillMaxSize()) {

                    val (screen, bottomNavigation) = createRefs()

                    Box(
                        Modifier
                            .padding(bottom = 48.dp)
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
                            onBackPressed = navController::popBackStack,
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
                        cartDisplayed = false,
                        modifier = Modifier
                            .wrapContentSize()
                            .constrainAs(bottomNavigation) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                                width = Dimension.fillToConstraints
                            },
                        parentNavigations = parentNavigations,
                        currentNavigation = Navigation.Wallet,
                        goTo = {
                            navController.navigate(it.path)
                        }
                    )
                }

            }
            composable(Navigation.Ticket.path) {

                val ticket: UserTicket = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get(Navigation.KEY_TICKET_ARG) as UserTicket? ?: UserTicket()

                TicketScreen(
                    ticket = ticket,
                    onBackPressed = navController::popBackStack,
                    download = { /*TODO*/ })
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
                        cartDisplayed = true,
                        modifier = Modifier
                            .wrapContentSize()
                            .constrainAs(bottomNavigation) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                                width = Dimension.fillToConstraints
                            },
                        parentNavigations = parentNavigations,
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
                    goToPayment = { voucher, paymentMethod ->
                        navController
                            .currentBackStackEntry
                            ?.savedStateHandle
                            ?.apply {
                                set(Navigation.KEY_PAYMENT_VOUCHER_ARG, voucher)
                                set(Navigation.KEY_PAYMENT_METHOD_ARG, paymentMethod)
                            }

                        navController.navigate(Navigation.Payment.path)
                    },
                    onBackPressed = navController::popBackStack
                )
            }

            composable(Navigation.Invoice.path) {
                PaymentInvoiceScreen(
                    onBackPressed = { navController.popBackStack() },
                    goToShop = { navController.navigate(Navigation.Shop.path) },
                    goToCart = { navController.navigate(Navigation.Cart.path) },
                )
            }

            composable(Navigation.Invoices.path) {
                PaymentInvoiceListScreen(
                    onBackPressed = navController::popBackStack, goToInvoice = {


                        navController.currentBackStackEntry
                            ?.savedStateHandle?.set(Navigation.KEY_INVOICE_ARG, it)

                        navController.navigate(Navigation.Invoice.path)
                    }
                )
            }

            composable(Navigation.Payment.path) {

                val savedStateHandle = navController
                    .previousBackStackEntry
                    ?.savedStateHandle

                val voucher = savedStateHandle?.get<String>(Navigation.KEY_PAYMENT_VOUCHER_ARG)
                val paymentMethod =
                    savedStateHandle?.get<PaymentMethod>(Navigation.KEY_PAYMENT_METHOD_ARG)

                requireNotNull(voucher)
                requireNotNull(paymentMethod)

                PaymentScreen(
                    onSuccess = {
                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set(Navigation.KEY_PAYMENT_STATUS_ARG, PaymentStatusEnum.SUCCESS)

                        navController.navigate(Navigation.Invoices.path)
                    },
                    onFailed = {

                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set(Navigation.KEY_PAYMENT_STATUS_ARG, PaymentStatusEnum.FAILED)

                        navController.navigate(Navigation.Invoices.path)
                    },
                    onCanceled = {
                        navController.popBackStack()
                    },
                    onBackPressed = navController::popBackStack
                )
            }

            composable(Navigation.AccountEditPassword.path) {
                AccountEditPasswordScreen(onBackPressed = navController::popBackStack)
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
                                navController.navigate(Navigation.Contact.path)
                            },
                            goToPrivacyPolicy = {

                                val privacyPolicyUrl = getString(R.string.privacy_policy_url)

                                navController
                                    .currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set(Navigation.KEY_WEBVIEW_ARG, privacyPolicyUrl)

                                navController.navigate(Navigation.WebView.path)
                            },
                            goToEditPassword = {
                                navController.navigate(Navigation.AccountEditPassword.path)
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
                        parentNavigations = parentNavigations,
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
        launcher.launch(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        cart.saveCart()
    }

    @Composable
    fun FloatingActionButton(modifier: Modifier = Modifier, goToCart: () -> Unit) {
        AnimatedVisibility(modifier = modifier, visible = true) {
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

    @Composable
    fun BottomNavigation(
        modifier: Modifier = Modifier,
        cartDisplayed: Boolean = false,
        parentNavigations: List<Navigation.ParentNavigation>,
        currentNavigation: Navigation,
        goTo: (Navigation) -> Unit,
    ) {


        val state by cart.state.collectAsState()

        ConstraintLayout(modifier = modifier.fillMaxSize()) {

            val (floatingActionButtonRef, bottomAppBarRef) = createRefs()

            if (cartDisplayed && state is CartState.Cart) {
                FloatingActionButton(
                    modifier =
                    Modifier.constrainAs(floatingActionButtonRef) {
                        bottom.linkTo(bottomAppBarRef.top, 24.dp)
                        end.linkTo(bottomAppBarRef.end, 24.dp)
                    },
                    goToCart = { goTo(Navigation.Cart) },
                )
            }

            BottomAppBar(
                modifier = modifier
                    .fillMaxWidth()
                    .constrainAs(bottomAppBarRef) {

                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .clip(shape = RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp)),
                containerColor = Color(0xFFFFCA00),

                ) {
                parentNavigations
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
    }
}
