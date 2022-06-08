package com.example.myshoplist.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop_list")
data class ShopItemDbModel (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val count: Int,
    val enabled: Boolean
)