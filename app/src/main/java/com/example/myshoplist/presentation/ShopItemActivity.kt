package com.example.myshoplist.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.myshoplist.R
import com.example.myshoplist.domain.ShopItem
import com.google.android.material.textfield.TextInputLayout
import java.lang.RuntimeException

class ShopItemActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishListener {

    private var screenMode = UNKNOWN_MODE
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)

        parseIntent()
        if (savedInstanceState == null) {
            launchRightScreenMode()
        }
    }

    private fun launchRightScreenMode() {
        val fragment = when (screenMode) {
            EXTRA_MODE_ADD -> ShopItemFragment.newInstanceAddMode()
            EXTRA_MODE_EDIT -> ShopItemFragment.newInstanceEditMode(shopItemId)
            else -> throw RuntimeException("\"intent has UNKNOWN EXTRA_MODE $screenMode")
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, fragment)
            .commit()
    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_MODE)) {
            throw RuntimeException("intent has not EXTRA_MODE")
        }
        val mode = intent.getStringExtra(EXTRA_MODE)
        if (mode != EXTRA_MODE_ADD && mode != EXTRA_MODE_EDIT) {
            throw RuntimeException("intent has UNKNOWN EXTRA_MODE $mode")
        }
        screenMode = mode.toString()
        if (mode == EXTRA_MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("intent has not EXTRA_SHOP_ITEM_ID")
            }
            shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    companion object {
        private const val EXTRA_MODE = "extra_mode"
        private const val EXTRA_MODE_ADD = "add_mode"
        private const val EXTRA_MODE_EDIT = "edit_mode"
        private const val EXTRA_SHOP_ITEM_ID = "shop_item_id"

        private const val UNKNOWN_MODE = ""

        fun newIntentAddMode(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_MODE, EXTRA_MODE_ADD)
            return intent
        }

        fun newIntentEditMode(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_MODE, EXTRA_MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            return intent
        }
    }

    override fun onEditingFinish() {
        Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
        finish()
    }
}