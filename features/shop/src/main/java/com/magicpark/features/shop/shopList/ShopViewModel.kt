package com.magicpark.features.shop.shopList

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


sealed interface ShopListState  {

    object Loading : ShopListState

    class ShopList(
        val items: List<ShopItem>,
        val categories: List<ShopCategory>
    ) : ShopListState

}


class ShopListViewModel : ViewModel() {

    companion object {
        private val TAG = ShopListViewModel::class.java.simpleName
    }

    private val shopUseCases: ShopUseCases by KoinJavaComponent.inject(ShopUseCases::class.java)
    private val cart: Cart by KoinJavaComponent.inject(Cart::class.java)

    private val _state: MutableStateFlow<ShopListState> = MutableStateFlow(ShopListState.Loading)

    val state: StateFlow<ShopListState>
        get() = _state

    init {
        viewModelScope.launch {
            val (items, categories) = shopUseCases.getShopItems()

            _state.value = ShopListState.ShopList(
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
