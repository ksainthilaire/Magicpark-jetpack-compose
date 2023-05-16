package com.magicpark.domain.repositories

import com.magicpark.domain.model.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable

interface IShopRepository {
    fun getShopCategories(): Observable<ShopCategory>
    fun getShopItems(): Observable<List<ShopItem>>
}
