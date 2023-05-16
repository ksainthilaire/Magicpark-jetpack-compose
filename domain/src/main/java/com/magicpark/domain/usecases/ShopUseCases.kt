package com.magicpark.domain.usecases

import com.magicpark.domain.model.ShopCategory
import com.magicpark.domain.model.ShopItem
import com.magicpark.domain.repositories.IShopRepository
import io.reactivex.rxjava3.core.Observable

class ShopUseCases(val repository: IShopRepository){

    fun getShopItems(): Observable<List<ShopItem>> = repository.getShopItems()
    fun getShopCategories(): Observable<ShopCategory> = repository.getShopCategories()

}