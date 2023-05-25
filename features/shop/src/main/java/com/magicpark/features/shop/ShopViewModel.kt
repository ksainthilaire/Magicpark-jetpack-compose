package com.magicpark.features.shop

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magicpark.domain.model.Cart
import com.magicpark.domain.model.Shop
import com.magicpark.domain.model.ShopItem
import com.magicpark.domain.usecases.ShopUseCases
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent


class ShopViewModel : ViewModel() {

    private val shopUseCases: ShopUseCases by KoinJavaComponent.inject(ShopUseCases::class.java)
    private val resources: Resources by KoinJavaComponent.inject(Resources::class.java)

    private val _state: MutableLiveData<ShopState> = MutableLiveData()
    val _shop: MutableLiveData<Shop> = MutableLiveData(Pair(listOf(), listOf()))

    val state: LiveData<ShopState>
        get() = _state

    val shop: LiveData<Shop>
        get() = _shop


    val _price: MutableLiveData<Float> = MutableLiveData(0f)


    val price: LiveData<Float>
        get() = _price



    val _cart: MutableLiveData<Cart> = MutableLiveData(ArrayList())


    val cart: LiveData<Cart>
        get() = _cart



    init {
        loadShopList()
    }

    private fun onShopItems(shop: Shop) {
        _shop.postValue(shop)
    }

    private fun onShopError(throwable: Throwable) {
        Log.d(TAG, "Shop error")
    }

    fun loadShopList() = viewModelScope.launch {
        shopUseCases.getShopItems()
            .subscribe(::onShopItems, ::onShopError)
    }

    private fun onShopItemAdded() {}

    fun addProduct(shopItem: ShopItem) = viewModelScope.launch {
        shopUseCases.addProduct(shopItem = shopItem)
            .subscribe(::onShopItemAdded, ::onShopError)
    }


    fun onCartLoaded(cart: Cart) {
        _cart.postValue(cart)
    }

    fun onCartError(throwable: Throwable) {
        Log.d(TAG, "Cart error")
    }

    fun clearCart() = viewModelScope.launch {
        shopUseCases.clearCart().doOnComplete {
                getCart()
            }
    }


    fun removeProduct(shopItem: ShopItem) = viewModelScope.launch {
        shopUseCases.removeProduct(shopItem).doOnComplete {
                getCart()
            }
    }
    
    fun getCart() = viewModelScope.launch {
        shopUseCases.getProducts().subscribe(::onCartLoaded, ::onCartError)
    }

    fun onTotalLoaded(price: Float) {
        _price.postValue(price)
    }

    fun getTotal() = viewModelScope.launch { 
        shopUseCases.getTotal()
            .subscribe(::onTotalLoaded, ::onCartError)
    }


    companion object {
        const val TAG = "ShopViewModel"
    }

}