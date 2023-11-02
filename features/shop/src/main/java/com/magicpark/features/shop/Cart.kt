package com.magicpark.features.shop

import android.content.Context
import com.magicpark.domain.model.ShopItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class Cart(context: Context) {

    private val _items: MutableStateFlow<List<ShopItem>> = MutableStateFlow(emptyList())

    val amount: Float
        get() {
            var total = 0f

            for (item in _items.value) {
                val price = item.price ?: 0f
                val quantity = item.quantity ?: 1

                total += price.times(quantity)
            }

            return total
        }

    val count: Int
        get() = _items.value.size

    val items: StateFlow<List<ShopItem>>
        get() = _items

    fun clearCart() {
        _items.value = emptyList()
    }

    fun removeProduct(shopItem: ShopItem) {
        val item = _items.value.find { item -> item.id ==  shopItem.id } ?: return
        val items = _items.value.toMutableList()
        val quantity = item.quantity ?: 0

        when {
            quantity > 1 -> {
                item.quantity = item.quantity?.minus(1)
            }
            else ->
                items.remove(item)
        }

        _items.value = items
    }

    fun addProduct(item: ShopItem) {
        val items = _items.value.toMutableList()
        items.add(item)

        _items.value = items
    }

    companion object {
        const val TAG = "MagicparkDbSession"

    }
}
