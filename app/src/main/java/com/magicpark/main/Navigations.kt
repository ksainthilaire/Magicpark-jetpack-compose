package com.magicpark.main

import com.magicpark.utils.R

sealed class Navigation(
    open val path: String,
) {

    companion object {
        const val KEY_WEBVIEW_ARG = "KEY_WEBVIEW_ARG"
        const val KEY_TICKET_ARG = "KEY_TICKET_ARG"
        const val KEY_SHOP_ARG = "KEY_SHOP_ARG"
    }

    sealed class ParentNavigation(
        /**
         * Specific path for this navigation
         */
        override val path: String,

        /**
         * Icon that can be displayed in navigation
         */
        val iconRes: Int,

        /**
         * Text to display for this navigation
         */
        val stringRes: Int,
    ) : Navigation(path = path)


    /**
     * Wallet screen
     */
    object Wallet : ParentNavigation(
        path = "wallet",
        iconRes = R.drawable.ic_ticket,
        stringRes = R.string.navigation_item_wallet,
    )

    /**
     * Shop screen
     */
    object Shop : ParentNavigation(

        path = "shop",
        iconRes = R.drawable.ic_shop,
        stringRes = R.string.navigation_item_shop,
    )

    /**
     * Settings screen
     */
    object Settings : ParentNavigation(
        path = "settings",
        iconRes = R.drawable.ic_search,
        stringRes = R.string.navigation_item_plus,
    )

    // Navigation to cart
    object Cart : Navigation("cart")

    // Navigating to the store item
    object ShopItem : Navigation("shop/{$KEY_TICKET_ARG}")

    // Navigating to a specific ticket
    object Ticket : Navigation("ticket")


    // Navigating to the contact screen
    object Contact : Navigation("contact")

    // Navigating to the contact screen
    object AccountSettings : Navigation("account/settings")

    // Navigating to payment screen
    object Payment : Navigation("payment/{paymentMethod}")

    object WebView : Navigation("webview")

    object Unknown : Navigation("unknown")
}




 val parentNavigations =
    listOf(Navigation.Wallet, Navigation.Shop, Navigation.Settings)

val navigations =
    parentNavigations +
    listOf(
        Navigation.Contact,
        Navigation.AccountSettings,
        Navigation.Payment,
        Navigation.ShopItem,
        Navigation.WebView,
        Navigation.Cart
    )

val Navigation.isParent: Boolean
    get() = parentNavigations.contains(this)

fun Navigation.Companion.find(path: String?) : Navigation =
    navigations.find { navigation -> navigation.path == path } ?: Navigation.Unknown
