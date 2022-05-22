package com.example.myshoplist.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myshoplist.R
import com.example.myshoplist.databinding.DisabledItemCardBinding
import com.example.myshoplist.databinding.EnabledItemCardBinding
import com.example.myshoplist.domain.ShopItem
import java.lang.RuntimeException
import java.util.zip.Inflater

class ShopListAdapter :
    ListAdapter<ShopItem, ShopListAdapter.ShopItemViewHolder>(ShopItemDiffCallback()) {

    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val layout = when(viewType) {
            ENABLED_ITEM -> R.layout.enabled_item_card
            DISABLED_ITEM -> R.layout.disabled_item_card
            else -> throw RuntimeException("Unknown viewType - $viewType")
        }
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layout,
            parent,
            false
        )
        return ShopItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {

        val shopItem = getItem(position)
        val binding = holder.binding
        when(binding) {
            is DisabledItemCardBinding -> {
                binding.shopItem = shopItem
            }
            is EnabledItemCardBinding -> {
                binding.shopItem = shopItem
            }
        }

        binding.root.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }

        binding.root.setOnClickListener {
            onShopItemClickListener?.invoke(shopItem)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.enabled) {
            ENABLED_ITEM
        } else DISABLED_ITEM
    }

    class ShopItemViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        const val ENABLED_ITEM = 1111
        const val DISABLED_ITEM = 2222
        const val MAX_POOL_SIZE = 10
    }
}