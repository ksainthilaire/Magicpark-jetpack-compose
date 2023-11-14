package com.magicpark.features.shop.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magicpark.domain.model.ShopItem
import com.magicpark.utils.ui.Cart
import com.magicpark.utils.ui.CartState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.java.KoinJavaComponent


sealed interface CartUiState  {

    val amount: Float

    /**
     * Shop Item List
     * @see [ShopItem]
     */
    val items: List<ShopItem>

    /**
     * @param items List of shop items
     */
    class Cart(
        override val amount: Float = 0F,
        override val items: List<ShopItem>,
    ) : CartUiState

}
class CartViewModel : ViewModel() {

    companion object {
        private val TAG = CartViewModel::class.java.simpleName
    }

    private val cart: Cart by KoinJavaComponent.inject(Cart::class.java)

    private val _state: MutableStateFlow<CartUiState> = MutableStateFlow(
        CartUiState.Cart(
            items = emptyList(),
        )
    )

    val state: StateFlow<CartUiState>
        get() = _state

    init {

        cart
            .state
            .onEach(::onCartStateChanged)
            .launchIn(viewModelScope)
    }

    private fun onCartStateChanged(state: CartState) {
        val newState = when (state) {
            is CartState.Cart ->
                CartUiState.Cart(amount = cart.amount, items = state.items)

            is CartState.Empty ->
                CartUiState.Cart(items = emptyList())
        }

        _state.value = newState
    }

    /**
     * Add a product
     * @param @see [ShopItem]
     */
    fun addProduct(shopItem: ShopItem) =
        cart.addCart(shopItem)

    /**
     * Remove a product
     * @param @see [ShopItem]
     */
    fun removeProduct(shopItem: ShopItem) =
        cart.removeProduct(shopItem = shopItem, allQuantity = false)

    /**
     * Remove all [shopItem] from the cart.
     * @param @see [ShopItem]
     */
    fun removeAllProduct(shopItem: ShopItem) =
        cart.removeProduct(shopItem = shopItem, allQuantity = true)
}
