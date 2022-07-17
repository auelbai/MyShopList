package com.example.myshoplist.presentation

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.myshoplist.R
import com.example.myshoplist.ShopListApp
import com.example.myshoplist.data.ShopListProvider
import com.example.myshoplist.databinding.ActivityMainBinding
import com.example.myshoplist.domain.ShopItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import javax.inject.Inject
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishListener {

    private val component by lazy {
        (application as ShopListApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdapter
    private lateinit var binding: ActivityMainBinding

    private var fragmentContainer: FragmentContainerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        component.inject(this)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonAddShopItem.setOnClickListener {
            if (isOnePaneMode()) {
                val intent = ShopItemActivity.newIntentAddMode(this)
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.newInstanceAddMode())
            }
        }

        setRecyclerView()
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        viewModel.shopList.observe(this) {
            shopListAdapter.submitList(it)
        }

        thread {
            val cursor = contentResolver.query(
                Uri.parse("content://com.example.myshoplist/shop_list"),
                null,
                null,
                null,
                null
            )

            while (cursor?.moveToNext() == true) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val count = cursor.getInt(cursor.getColumnIndexOrThrow("count"))
                val enabled = cursor.getInt(cursor.getColumnIndexOrThrow("enabled")) > 0
                val shopItem = ShopItem(
                    id = id,
                    name = name,
                    count = count,
                    enabled = enabled
                )
                Log.d("MainActivity", shopItem.toString())
            }
            cursor?.close()
        }
    }

    private fun setRecyclerView() {
        shopListAdapter = ShopListAdapter()
        with(binding.rvShopList) {
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
        setUpSwipeListener(binding.rvShopList)
    }

    private fun setUpLongClickListener() {
        shopListAdapter.onShopItemLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }

    private fun setUpClickListener() {

        shopListAdapter.onShopItemClickListener = {
            if (isOnePaneMode()) {
                val intent = ShopItemActivity.newIntentEditMode(this, it.id)
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.newInstanceEditMode(it.id))
            }
        }
    }

    private fun isOnePaneMode(): Boolean {
        return fragmentContainer == null
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_view_fragment, fragment)
            .addToBackStack(null)
            .commit()
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
                //viewModel.deleteShopItem(item)

                thread {
                    contentResolver.delete(
                        Uri.parse("content://com.example.myshoplist/shop_list"),
                        null,
                        arrayOf(item.id.toString())
                    )
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onEditingFinish() {
        Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
        supportFragmentManager.popBackStack()
    }
}