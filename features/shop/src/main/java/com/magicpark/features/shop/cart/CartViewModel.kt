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
     * Total cart amount
     */
    val amount: Float

    /**
     * Discount amount after a voucher applied
     */
    val voucher: Float

    /**
     * Shop Item List
     * @see [ShopItem]
     */
    val items: List<ShopItem>

    /**
     *  List of shop categories
     *  @see [ShopCategory]
     */
    val categories: List<ShopCategory>

    /**
     * Selected shop category
     */
    val currentCategory: Long


    /**
     * @param items List of shop items
     * @param categories List of shop categories
     */
    class Cart(

        override val currentCategory: Long,

        override val amount: Float,
        override val voucher: Float,

        override val items: List<ShopItem>,
        override val categories: List<ShopCategory>
    ) : CartState

}
class CartViewModel : ViewModel() {
    companion object {
        private val TAG = CartViewModel::class.java.simpleName
    }

    private val shopUseCases: ShopUseCases by KoinJavaComponent.inject(ShopUseCases::class.java)
    private val cart: Cart by KoinJavaComponent.inject(Cart::class.java)

    private val _state: MutableStateFlow<CartState> = MutableStateFlow(CartState.Cart(
        currentCategory = 0L,
        amount = 0F,
        voucher = 0F,
        items = emptyList(),
        categories = emptyList(),
    ))

    val state: StateFlow<CartState>
        get() = _state

    init {
        viewModelScope.launch {
            val (items, categories) = shopUseCases.getShopItems()

            _state.value = CartState.Cart(
                currentCategory = 0L,
                amount = 10F,
                voucher = 10F,
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

    fun removeProduct(id: Long) {
        TODO()
    }

}
