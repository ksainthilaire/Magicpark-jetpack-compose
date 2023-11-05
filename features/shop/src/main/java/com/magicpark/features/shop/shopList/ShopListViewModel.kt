package com.magicpark.features.shop.shopList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magicpark.domain.model.ShopCategory
import com.magicpark.domain.model.ShopItem
import com.magicpark.domain.usecases.ShopUseCases
import com.magicpark.features.shop.Cart
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent


sealed interface ShopListState {

    val items: List<ShopItem>
    val categories: List<ShopCategory>

    class ShopList(
        override val items: List<ShopItem> = emptyList(),
        override val categories: List<ShopCategory> = emptyList(),
        val currentCategory: Long = 0L
    ) : ShopListState

}


class ShopListViewModel : ViewModel() {

    companion object {
        private val TAG = ShopListViewModel::class.java.simpleName
    }

    private val shopUseCases: ShopUseCases by KoinJavaComponent.inject(ShopUseCases::class.java)
    private val cart: Cart by KoinJavaComponent.inject(Cart::class.java)

    private var items: List<ShopItem> =
        emptyList()

    private var categories: List<ShopCategory> =
        emptyList()

    private val currentStringSearch: MutableSharedFlow<String> = MutableStateFlow("")
    private val currentCategory: MutableSharedFlow<Long> = MutableStateFlow(0L)

    val state: StateFlow<ShopListState> = combine(currentCategory, currentStringSearch)
    { category,
      stringSearch ->

        ShopListState.ShopList(
            items = items,//.filter { it.categories?.contains(category) ?: false },
            categories = categories,
        )
    }
        .onStart {
            fetchShopItems()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            ShopListState.ShopList(emptyList(), emptyList())
        )

    private suspend fun fetchShopItems() {
        val response = shopUseCases.getShopItems()

        items = response.first
        categories = response.second
    }

    fun changeCategory(category: Long) {
        val indexes = categories.map { it.id ?: 0L }
        if (category !in indexes) return

        viewModelScope.launch {
            currentCategory.emit(category)
        }
    }
}
