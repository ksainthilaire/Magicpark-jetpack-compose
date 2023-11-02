package com.magicpark.features.shop.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magicpark.domain.model.ShopCategory
import com.magicpark.domain.model.ShopItem
import com.magicpark.domain.usecases.ShopUseCases
import com.magicpark.features.shop.Cart
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent


sealed interface CartState  {

    /**
     * Initial state
     */
    object Loading : CartState

    /**
     * @param items List of shop items
     * @param categories List of shop categories
     */
    class Cart(
        val items: List<ShopItem>,
        val categories: List<ShopCategory>
    ) : CartState

}
class CartViewModel : ViewModel() {
    companion object {
        private val TAG = CartViewModel::class.java.simpleName
    }

    private val shopUseCases: ShopUseCases by KoinJavaComponent.inject(ShopUseCases::class.java)
    private val cart: Cart by KoinJavaComponent.inject(Cart::class.java)

    private val _state: MutableStateFlow<CartState> = MutableStateFlow(CartState.Loading)

    val state: StateFlow<CartState>
        get() = _state
    init {
        viewModelScope.launch {
            val (items, categories) = shopUseCases.getShopItems()

            _state.value = CartState.Cart(
                items = items,
                categories = categories,
            )
        }
    }

    /**
     * Add a product
     * @param shopItem
     */
    fun addProduct(shopItem: ShopItem) =
        cart.addProduct(shopItem)

    /**
     * Remove a product
     * @param shopItem
     */
    fun removeProduct(shopItem: ShopItem) =
        cart.removeProduct(shopItem)

}
