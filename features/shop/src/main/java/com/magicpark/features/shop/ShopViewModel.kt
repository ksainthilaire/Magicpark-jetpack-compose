package com.magicpark.features.shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magicpark.domain.model.ShopItem
import com.magicpark.domain.usecases.ShopUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent


class ShopViewModel : ViewModel() {

    companion object {
        private val TAG = ShopViewModel::class.java.simpleName
    }

    private val shopUseCases: ShopUseCases by KoinJavaComponent.inject(ShopUseCases::class.java)
    private val cart: Cart by KoinJavaComponent.inject(Cart::class.java)

    private val _state: MutableStateFlow<ShopState> = MutableStateFlow(ShopState.Loading)

    val state: StateFlow<ShopState>
        get() = _state

    init {
        viewModelScope.launch {
            val (items, categories) = shopUseCases.getShopItems()

            _state.value = ShopState.ShopList(
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
