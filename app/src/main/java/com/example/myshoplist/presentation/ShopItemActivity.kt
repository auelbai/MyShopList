package com.example.myshoplist.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.myshoplist.R
import com.example.myshoplist.domain.ShopItem
import com.google.android.material.textfield.TextInputLayout
import java.lang.RuntimeException

class ShopItemActivity : AppCompatActivity() {

    private lateinit var nameTIL: TextInputLayout
    private lateinit var nameET: EditText
    private lateinit var countTIL: TextInputLayout
    private lateinit var countET: EditText
    private lateinit var saveButton: Button

    private lateinit var viewModel: ShopItemViewModel
    private var screenMode = UNKNOWN_MODE
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)

        parseIntent()
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        initViews()
        addTextChangeListener()
        launchRightScreenMode()
        viewModelsObservers()
    }

    private fun viewModelsObservers() {
        viewModel.errorInputCount.observe(this) {
            val message = if (it) {
                getString(R.string.countTILError)
            } else {
                null
            }
            countTIL.error = message
        }

        viewModel.errorInputName.observe(this) {
            val message = if (it) {
                getString(R.string.nameTILError)
            } else {
                null
            }
            nameTIL.error = message
        }

        viewModel.shouldCloseScreen.observe(this) {
            finish()
        }
    }

    private fun addTextChangeListener() {
        nameET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        countET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun launchRightScreenMode() {
        when (screenMode) {
            EXTRA_MODE_ADD -> launchAddScreenMode()
            EXTRA_MODE_EDIT -> launchEditScreenMode()
        }
    }

    private fun launchAddScreenMode() {
        saveButton.setOnClickListener {
            val name = nameET.text?.toString()
            val count = countET.text?.toString()
            viewModel.addShopItem(name, count)
        }
    }

    private fun launchEditScreenMode() {
        viewModel.getShopItem(shopItemId)
        viewModel.shopItem.observe(this) {
            nameET.setText(it.name)
            countET.setText(it.count.toString())
        }

        saveButton.setOnClickListener {
            val name = nameET.text?.toString()
            val count = countET.text?.toString()
            viewModel.editShopItem(name, count)
        }

    }

    private fun initViews() {
        nameTIL = findViewById(R.id.til_name)
        nameET = findViewById(R.id.et_name)
        countTIL = findViewById(R.id.til_count)
        countET = findViewById(R.id.et_count)
        saveButton = findViewById(R.id.save_button)
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
}