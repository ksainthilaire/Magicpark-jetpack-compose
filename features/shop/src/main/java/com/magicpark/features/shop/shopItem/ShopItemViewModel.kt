package com.magicpark.features.shop.shopItem

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.magicpark.domain.model.ShopItem
import com.magicpark.features.shop.Cart
import org.koin.java.KoinJavaComponent

class ShopItemViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    companion object {
        private const val KEY_SHOP_ARG = "KEY_SHOP_ARG"
    }

    private val cart: Cart by KoinJavaComponent.inject(Cart::class.java)

    val shopItem: ShopItem
        get() = savedStateHandle.get<ShopItem>(KEY_SHOP_ARG) ?: ShopItem()

    /**
     * Add a product
     * @param @see [ShopItem]
     */
    fun addProduct(shopItem: ShopItem) =
        cart.addCart(shopItem)
}
