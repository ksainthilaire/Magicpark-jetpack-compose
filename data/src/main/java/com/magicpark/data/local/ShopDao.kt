package com.magicpark.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.magicpark.domain.model.ShopCategory
import com.magicpark.domain.model.ShopItem
import io.reactivex.rxjava3.core.Observable


@Dao
interface ShopDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveShopItems(items: List<ShopItem>): List<Long>

    @Query("SELECT * FROM shop WHERE category LIKE :category")
    fun getShopItemsByCategory(category: ShopCategory): Observable<List<ShopItem>>

    @Query("SELECT * FROM shop WHERE id LIKE :id LIMIT 1")
    fun getShopItem(id: Int): Observable<ShopItem>
}
