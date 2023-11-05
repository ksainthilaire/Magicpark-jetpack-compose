package com.magicpark.features.shop.shopItem

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.magicpark.domain.model.ShopCategory
import com.magicpark.domain.model.ShopItem
import com.magicpark.domain.usecases.ShopUseCases
import com.magicpark.features.shop.Cart
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.java.KoinJavaComponent


sealed interface ShopItemState  {

    object Loading : ShopItemState

    class ShopList(
        val items: List<ShopItem>,
        val categories: List<ShopCategory>
    ) : ShopItemState

}


class ShopItemViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    companion object {
        private const val KEY_SHOP_ITEM = "KEY-SHOP-ITEM"
        private val TAG = ShopItemViewModel::class.java.simpleName
    }

    private val shopUseCases: ShopUseCases by KoinJavaComponent.inject(ShopUseCases::class.java)
    private val cart: Cart by KoinJavaComponent.inject(Cart::class.java)

    private val _state: MutableStateFlow<ShopItemState> = MutableStateFlow(ShopItemState.Loading)

    val state: StateFlow<ShopItemState>
        get() = _state

    val shopItem: ShopItem
        get() = savedStateHandle.get<ShopItem>(KEY_SHOP_ITEM) ?: ShopItem()

    /**
     * Add a product
     * @param shopItem
     */
    fun addProduct(shopItem: ShopItem) =
        cart.addCart(shopItem)

    /**
     * Remove a product
     * @param shopItem
     */
    fun removeProduct(shopItem: ShopItem) =
        cart.removeProduct(shopItem)
}
