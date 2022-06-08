package com.example.myshoplist.data

import android.os.FileObserver.DELETE
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShopListDao {

    @Query("SELECT * FROM shop_list")
    fun getShopListDao() : LiveData<List<ShopItemDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addShopItemDao(shopItemDbModel: ShopItemDbModel)

    @Query("DELETE FROM shop_list WHERE id = :shopItemId")
    fun deleteShopItemDao(shopItemId: Int)

    @Query("SELECT * FROM shop_list WHERE id = :shopItemId LIMIT 1")
    fun getShopItemDao(shopItemId: Int) : ShopItemDbModel
}