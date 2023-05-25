package com.magicpark.data.session

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.magicpark.domain.enums.PaymentMethodEnum
import com.magicpark.domain.model.Cart
import com.magicpark.domain.model.ShopItem

class MagicparkDbSession(context: Context) {


    private var prefs: SharedPreferences =
        context.getSharedPreferences(KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE)

    /* Splash screen */

    fun setWelcome() {
        val editor = prefs.edit()
        editor.putBoolean(KEY_API_WELCOME, true)
        editor.apply { }
    }

    fun getWelcome(): Boolean {
        return prefs.getBoolean(KEY_API_WELCOME, false)
    }

    /* Session */

    fun saveToken(token: String) {
        val editor = prefs.edit()
        editor.putString(KEY_API_TOKEN, token)
        editor.apply()
    }

    fun clearToken() {
        val editor = prefs.edit()
        editor.remove(KEY_API_TOKEN)
        editor.apply()
    }

    fun getToken(): String {
        return prefs.getString(KEY_API_TOKEN, null)
            ?: throw Exception("The device does not have a token")
    }

    fun isLogged(): Boolean =
        try {
            val token = getToken()
            Log.d(TAG, "token = ${token}")
            true
        } catch (e: Exception) {
            false
        }


    /* Cart */


    private val cart: Cart = Cart()
    private val counter: MutableLiveData<Pair<Int, Float>> = MutableLiveData(Pair(0, 0f))

    fun clearCart() = cart.clear()

    fun listenCounter(lifecycle: LifecycleOwner, observer: Observer<Pair<Int, Float>>) =
        counter.observe(lifecycle, observer)

    fun getProducts(): Cart = cart

    fun removeProduct(shopItem: ShopItem, allQuantities: Boolean = false) {
        cart.find { it.id == shopItem.id }?.let { product ->
            if (allQuantities || product.quantity!! <= 1) {
                cart.remove(product)
            } else product.quantity = product.quantity!!.minus(1)
        }
        updateCart()
    }

    fun addProduct(shopItem: ShopItem) {
        val product = cart.find { it.id == shopItem.id }
        if (product != null) {
            product.quantity = product.quantity?.plus(1)
        } else cart.add(shopItem.copy(quantity = 1))
        updateCart()
    }

    private fun updateCart() {
        counter.postValue(Pair(cart.size, getTotal()))
    }

    fun getTotal(): Float {
        var total = 0f
        for (item in cart) {
            val price = item.price
            total += price?.times(item.quantity!!) ?: 0f
        }
        return total
    }

    companion object {

        const val TAG = "MagicparkDbSession"

        const val KEY_SHARED_PREFERENCES = "KEY-MAGICPARK"
        const val KEY_API_TOKEN = "KEY-API-TOKEN"
        const val KEY_API_WELCOME = "KEY-API-WELCOME"

        const val KEY_CART = "KEY-CART"
    }
}
