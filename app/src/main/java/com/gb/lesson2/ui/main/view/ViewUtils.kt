package com.gb.lesson2.ui.main.view

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.snackbar.Snackbar
import java.lang.RuntimeException

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.showSnackBar(
    text: String,
    actionText: String,
    action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar
        .make(
            this,
            text,
            length
        )
        .setAction(actionText) { action(this) }
        .show()
}

fun View.hideKeyboard(): Boolean = try {
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
} catch (e: RuntimeException) {
    false
}