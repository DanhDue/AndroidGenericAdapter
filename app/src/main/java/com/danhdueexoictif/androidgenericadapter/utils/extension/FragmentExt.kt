package com.danhdueexoictif.androidgenericadapter.utils.extension

import android.app.Activity
import android.content.DialogInterface
import android.util.DisplayMetrics
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Fragment.hideKeyBoard() {
    activity?.apply {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this@hideKeyBoard.view?.windowToken, 0)
    }
}

fun Fragment.showCommonDialog(
    title: String? = null, message: String? = null,
    positiveTextId: Int, positiveAction: ((DialogInterface?, Int) -> Unit)? = null,
    negativeTextId: Int, negativeAction: ((DialogInterface?, Int) -> Unit)? = null
) {
    val builder = MaterialAlertDialogBuilder(requireContext())
    title?.let { builder.setTitle(title) }
    message?.let { builder.setMessage(message) }
    builder.setPositiveButton(positiveTextId) { dialogInterface, i ->
        positiveAction?.invoke(dialogInterface, i)
    }
    builder.setNegativeButton(negativeTextId) { dialogInterface, i ->
        negativeAction?.invoke(dialogInterface, i)
    }
    builder.show()
}

fun Fragment.getScreenSize(): Pair<Int, Int> {
    activity?.let {
        val displayMetrics = DisplayMetrics()
        it.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return Pair(displayMetrics.widthPixels, displayMetrics.heightPixels)
    }
    return Pair(0, 0)
}

fun Fragment.showToast(s: String) {
    Toast.makeText(this.context, s, Toast.LENGTH_SHORT).show()
}
