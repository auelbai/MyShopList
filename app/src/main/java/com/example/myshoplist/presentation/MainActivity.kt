package com.example.myshoplist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.myshoplist.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdapter

    private var fragmentContainer: FragmentContainerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentContainer = findViewById(R.id.container_view_fragment)
        val itemAddBtn = findViewById<FloatingActionButton>(R.id.button_add_shop_item)


        itemAddBtn.setOnClickListener {
            if (fragmentContainer == null) {
                val intent = ShopItemActivity.newIntentAddMode(this)
                startActivity(intent)
            } else {
                val fragment = ShopItemFragment.newInstanceAddMode()
                supportFragmentManager.beginTransaction()
                    .add(R.id.container_view_fragment, fragment)
                    .commit()
            }
        }


        setRecyclerView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopList.observe(this) {
            shopListAdapter.submitList(it)
        }
    }

    private fun setRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.rv_shop_list)
        shopListAdapter = ShopListAdapter()
        with(recyclerView) {
            adapter = shopListAdapter
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.DISABLED_ITEM,
                ShopListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.ENABLED_ITEM,
                ShopListAdapter.MAX_POOL_SIZE
            )
        }
        setUpLongClickListener()
        setUpClickListener()
        setUpSwipeListener(recyclerView)
    }

    private fun setUpLongClickListener() {
        shopListAdapter.onShopItemLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }

    private fun setUpClickListener() {

        shopListAdapter.onShopItemClickListener = {
            if (fragmentContainer == null) {
                val intent = ShopItemActivity.newIntentEditMode(this, it.id)
                startActivity(intent)
            } else {
                val fragment = ShopItemFragment.newInstanceEditMode(it.id)
                supportFragmentManager.beginTransaction()
                    .add(R.id.container_view_fragment, fragment)
                    .commit()
            }
        }
    }

    private fun setUpSwipeListener(recyclerView: RecyclerView?) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = shopListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

}