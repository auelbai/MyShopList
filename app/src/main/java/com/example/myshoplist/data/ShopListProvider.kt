package com.example.myshoplist.data

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.example.myshoplist.ShopListApp
import com.example.myshoplist.domain.ShopItem
import javax.inject.Inject

class ShopListProvider: ContentProvider() {

    private val component by lazy {
        (context as ShopListApp).component
    }

    @Inject
    lateinit var shopListDao: ShopListDao

    @Inject
    lateinit var mapper: Mapper

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITY, PATH_TABLE, PATH_TABLE_CODE)
        addURI(AUTHORITY, PATH_ID, PATH_ID_CODE)
    }

    override fun onCreate(): Boolean {
        component.inject(this)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return when(uriMatcher.match(uri)) {
            PATH_TABLE_CODE -> {
                shopListDao.getShopListDaoCursor()
            }
            else -> {
                null
            }
        }
    }

    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        when(uriMatcher.match(uri)) {
            PATH_TABLE_CODE -> {
                if (values == null) return null
                val id = values.getAsInteger("id")
                val name = values.getAsString("name")
                val count = values.getAsInteger("count")
                val enabled = values.getAsBoolean("enabled")

                val shopItem = ShopItem(
                    name, count, enabled, id
                )
                shopListDao.addShopItemDaoProvider(mapper.mapEntityToDbModel(shopItem))
            }
        }
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        when(uriMatcher.match(uri)) {
            PATH_TABLE_CODE -> {
                val id = selectionArgs?.get(0)?.toInt() ?: -1
                shopListDao.deleteShopItemDaoProvider(id)
            }
        }
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        when(uriMatcher.match(uri)) {
            PATH_TABLE_CODE -> {
                val itemId = selectionArgs?.get(0)?.toInt() ?: -1
                val name = values?.getAsString("name") ?: ""
                val count = values?.getAsInteger("count") ?: -1
                val enabled = values?.getAsBoolean("enabled") ?: true

                val shopItem = ShopItem(
                    name, count, enabled, itemId
                )
                shopListDao.addShopItemDaoProvider(mapper.mapEntityToDbModel(shopItem))
            }
        }
        return 0
    }

    companion object {
        const val AUTHORITY = "com.example.myshoplist"
        const val PATH_TABLE = "shop_list"
        const val PATH_TABLE_CODE = 100
        const val PATH_ID = "shop_list/#"
        const val PATH_ID_CODE = 101
    }
}