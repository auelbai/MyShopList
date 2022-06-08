package com.example.myshoplist.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.myshoplist.domain.ShopItem
import com.example.myshoplist.domain.ShopListRepository
import java.lang.RuntimeException
import java.util.*
import kotlin.Comparator
import kotlin.random.Random

class ShopListRepositoryImpl(
    application: Application
): ShopListRepository {

    private val shopListDao = AppDatabase.getInstance(application).shopListDao()
    private val mapper = Mapper()

    override suspend fun addShopItem(shopItem: ShopItem) {
        shopListDao.addShopItemDao(mapper.mapEntityToDbModel(shopItem))
    }

    override suspend fun deleteShopItem(shopItem: ShopItem) {
        shopListDao.deleteShopItemDao(shopItem.id)
    }

    override suspend fun editShopItem(shopItem: ShopItem) {
        shopListDao.addShopItemDao(mapper.mapEntityToDbModel(shopItem))
    }

    override suspend fun getShopItem(id: Int): ShopItem {
        val dbModelItem = shopListDao.getShopItemDao(id)
        return mapper.mapDbModelToEntity(dbModelItem)
    }

    override fun getShopList(): LiveData<List<ShopItem>> = Transformations.map(
        shopListDao.getShopListDao()
    ) {
        mapper.mapListDbModelToList(it)
    }
}