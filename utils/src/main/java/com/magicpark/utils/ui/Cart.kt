package com.magicpark.utils.ui

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.magicpark.core.Config
import com.magicpark.domain.model.ShopItem
import com.magicpark.domain.model.currentPrice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface CartState {
    /**
     * The cart is empty.
     */
    object Empty : CartState

    /**
     * Items are present in the basket.
     * @param items List of shop items
     */
    data class Cart(
        val items: List<ShopItem>
    ) : CartState
}

class Cart(context: Context) {

    companion object {
        const val KEY_CART = "KEY-CART"
        private val TAG = Cart::class.java.simpleName
    }

    val amount: Float
        get() {
            if (state.value !is CartState.Cart) return 1337F
            val state = state.value as CartState.Cart
            var total = 0F

            for (item in state.items) {
                val price = item.currentPrice
                val quantity = item.quantity ?: 1

                total += price?.times(quantity) ?: 0F
            }
            return total
        }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(
            Config.KEY_SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )

    private val coroutineScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main
    )

    private val _state: MutableStateFlow<CartState> = MutableStateFlow(CartState.Empty)
    val state: StateFlow<CartState> = _state

    init { fetchCart() }

    fun removeProduct(shopItem: ShopItem, allQuantity: Boolean = false) {

        if (!allQuantity) {
            removeProduct(shopItem = shopItem, quantity = 1)
            return
        }

        if (state.value !is CartState.Cart) {
            Log.e(TAG, "Cannot delete a product if the basket is empty")
            return
        }

        val state = state.value as CartState.Cart
        val item = state.items.find { item -> item.id == shopItem.id } ?: return

        removeProduct(shopItem = shopItem, quantity = item.quantity ?: 0)
    }

    private fun removeProduct(shopItem: ShopItem, quantity: Int)  = coroutineScope.launch {

        Log.i(TAG, "Deleting the product from the cart. name = ${shopItem.name}")

        if (state.value !is CartState.Cart) {
            Log.e(TAG, "Cannot delete a product if the basket is empty")
            return@launch
        }
        val state = state.value as CartState.Cart

        val item = state.items.find { item -> item.id == shopItem.id } ?: return@launch
        val items = state.items.toMutableList()
        val itemQuantity = item.quantity ?: 0

         when {
            itemQuantity > quantity -> {
                items.apply {
                    remove(item)
                    add(
                        item.copy(quantity = item.quantity?.minus(quantity))
                    )
                }
            }

            else ->
                items.remove(item)
        }

        _state.value =
            if (items.size < 1) CartState.Empty else CartState.Cart(items = items)
    }

    /**
     * Add a product to the cart.
     * @param @see [ShopItem]
     */
    fun addCart(shopItem: ShopItem) = coroutineScope.launch {

        Log.i(TAG, "Add a product to the cart. name = ${shopItem.name}")

        when (val state = state.value) {
            is CartState.Empty -> {
                _state.value = CartState.Cart(
                    listOf(shopItem.copy(quantity = 1))
                )
            }

            is CartState.Cart -> {
                val items = state.items.toMutableList()
                val item = items.find { item -> item.id == shopItem.id }

                if (item == null) {
                    items.add(shopItem.copy(quantity = 1))
                    _state.value = CartState.Cart(items = items)
                    return@launch
                }
                items.remove(item)
                items.add(
                    item.copy(quantity = item.quantity?.plus(1))
                )

                _state.value = CartState.Cart(items)
            }
        }
    }

    /**
     * Fetch the cart.
     */
    private fun fetchCart() {

        Log.i(TAG, "Recovery of the basket saved locally.")

        val json = sharedPreferences
            .getString(KEY_CART, null)

        if (json == null) {
            Log.e(
                TAG, "The basket is empty, nothing " +
                    "was saved locally during the previous session.")
            _state.value = CartState.Empty
            return
        }

        Log.i(TAG, "Cart recovered successfully. json = $json.")

        val itemsType = object : TypeToken<List<ShopItem>>() {}.type
        val items: List<ShopItem> = Gson().fromJson(json, itemsType)

        coroutineScope.launch {
            _state.value = CartState.Cart(items = items)
        }
    }

    /**
     * Save a product in the cart.
     */
    fun saveCart() {

        if (state.value !is CartState.Cart) {
            Log.e(TAG, "Unable to save the basket because it is empty.")
            return
        }

        val state = state.value as CartState.Cart

        val json = Gson().toJson(state.items)

        sharedPreferences.edit()
            .putString(KEY_CART, json)
            .apply()

        Log.i(TAG, "Saved cart successfully.")
    }

    /**
     * Delete a product in the cart.
     */
    fun removeCart() {

        if (state.value !is CartState.Cart) {
            Log.e(TAG, "Unable to clear the basket because it is empty.")
            return
        }

        sharedPreferences
            .edit()
            .remove(KEY_CART)
            .apply()

        Log.i(TAG, "Delete cart successful.")

        coroutineScope.launch {
           _state.value = CartState.Empty
        }
    }
}
