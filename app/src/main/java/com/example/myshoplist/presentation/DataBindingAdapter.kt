package com.example.myshoplist.presentation

import androidx.databinding.BindingAdapter
import com.example.myshoplist.R
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("errorInputCount")
fun bindErrorInputCount(textInputLayout: TextInputLayout, error: Boolean) {
    val message = if (error) {
        textInputLayout.context.getString(R.string.countTILError)
    } else {
        null
    }
    textInputLayout.error = message
}

@BindingAdapter("errorInputName")
fun bindErrorInputName(textInputLayout: TextInputLayout, error: Boolean) {
    val message = if (error) {
        textInputLayout.context.getString(R.string.nameTILError)
    } else {
        null
    }
    textInputLayout.error = message
}