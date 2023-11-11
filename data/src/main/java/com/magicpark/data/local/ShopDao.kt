package com.magicpark.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.magicpark.domain.model.ShopCategory
import com.magicpark.domain.model.ShopItem
import com.magicpark.domain.model.UserTicket
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveShopCategories(items: List<ShopCategory>): List<Long>

    @Query("SELECT * FROM shop_category")
    fun getShopCategories(): List<ShopCategory>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveShopItems(items: List<ShopItem>): List<Long>

    @Query("SELECT * FROM shop")
    fun getShopItems(): List<ShopItem>


    @Query("SELECT * FROM shop WHERE id LIKE :id LIMIT 1")
    fun getShopItem(id: Int): Observable<ShopItem>
}
