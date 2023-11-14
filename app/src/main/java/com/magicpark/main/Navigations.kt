package com.magicpark.main

import com.magicpark.utils.R

sealed class Navigation(
    open val path: String,
) {

    companion object {
        const val KEY_WEBVIEW_ARG = "KEY_WEBVIEW_ARG"
        const val KEY_TICKET_ARG = "KEY_TICKET_ARG"
        const val KEY_SHOP_ARG = "KEY_SHOP_ARG"
        const val KEY_INVOICE_ARG = "KEY_INVOICE_ARG"
        const val KEY_PAYMENT_STATUS_ARG = "KEY_PAYMENT_STATUS_ARG"


        const val KEY_PAYMENT_METHOD_ARG = "KEY_PAYMENT_METHOD_ARG"
        const val KEY_PAYMENT_VOUCHER_ARG = "KEY_VOUCHER_ARG"
        const val KEY_PAYMENT_SHOP_ITEMS_ARG = "KEY_PAYMENT_SHOP_ITEMS_ARG"
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

    // Navigating to account settings
    object AccountSettings : Navigation("account/settings")

    // Navigating to password settings
    object AccountEditPassword : Navigation("account/edit-password")

    // Navigating to payment screen
    object Payment : Navigation("payment/{paymentMethod}")

    object WebView : Navigation("webview")

    object Unknown : Navigation("unknown")

    object Invoices : Navigation("invoices")
    object Invoice : Navigation("invoice")
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
