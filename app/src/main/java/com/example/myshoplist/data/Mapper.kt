package com.example.myshoplist.data

import com.example.myshoplist.domain.ShopItem

class Mapper {

    fun mapDbModelToEntity(shopItemDbModel: ShopItemDbModel) = ShopItem(
        id = shopItemDbModel.id,
        name = shopItemDbModel.name,
        count = shopItemDbModel.count,
        enabled = shopItemDbModel.enabled
    )

    fun mapEntityToDbModel(shopItem: ShopItem) = ShopItemDbModel(
        id = shopItem.id,
        name = shopItem.name,
        count = shopItem.count,
        enabled = shopItem.enabled
    )

    fun mapListDbModelToList(list: List<ShopItemDbModel>): List<ShopItem> = list.map {
        mapDbModelToEntity(it)
    }
}