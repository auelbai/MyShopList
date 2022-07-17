package com.example.myshoplist.data

import android.database.Cursor
import android.os.FileObserver.DELETE
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShopListDao {

    @Query("SELECT * FROM shop_list")
    fun getShopListDao() : LiveData<List<ShopItemDbModel>>

    @Query("SELECT * FROM shop_list")
    fun getShopListDaoCursor() : Cursor

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addShopItemDao(shopItemDbModel: ShopItemDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addShopItemDaoProvider(shopItemDbModel: ShopItemDbModel)

    @Query("DELETE FROM shop_list WHERE id = :shopItemId")
    suspend fun deleteShopItemDao(shopItemId: Int)

    @Query("DELETE FROM shop_list WHERE id = :shopItemId")
    fun deleteShopItemDaoProvider(shopItemId: Int): Int

    @Query("SELECT * FROM shop_list WHERE id = :shopItemId LIMIT 1")
    suspend fun getShopItemDao(shopItemId: Int) : ShopItemDbModel
}