package com.magicpark.features.shop.shopItem

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


sealed interface ShopItemState  {

    object Loading : ShopItemState

    class ShopList(
        val items: List<ShopItem>,
        val categories: List<ShopCategory>
    ) : ShopItemState

}


class ShopItemViewModel : ViewModel() {

    companion object {
        private val TAG = ShopItemViewModel::class.java.simpleName
    }

    private val shopUseCases: ShopUseCases by KoinJavaComponent.inject(ShopUseCases::class.java)
    private val cart: Cart by KoinJavaComponent.inject(Cart::class.java)

    private val _state: MutableStateFlow<ShopItemState> = MutableStateFlow(ShopItemState.Loading)

    val state: StateFlow<ShopItemState>
        get() = _state

    init {
        viewModelScope.launch {
            val (items, categories) = shopUseCases.getShopItems()

            _state.value = ShopItemState.ShopList(
                items = items,
                categories = categories,
            )
        }
    }

    fun addProduct(shopItem: ShopItem) =
        cart.addProduct(shopItem)

    fun removeProduct(shopItem: ShopItem) =
        cart.removeProduct(shopItem)

}
