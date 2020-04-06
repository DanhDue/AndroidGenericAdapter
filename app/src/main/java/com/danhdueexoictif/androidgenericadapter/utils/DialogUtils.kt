package com.danhdueexoictif.androidgenericadapter.utils

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.danhdueexoictif.androidgenericadapter.ui.widgets.ErrorDialog
import com.danhdueexoictif.androidgenericadapter.ui.widgets.LoadingDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object DialogUtils {

    fun createErrorDialog(context: Context, errorState: ErrorState?): ErrorDialog =
        ErrorDialog(context).also { it.errorState = errorState }

    fun showLoadingDialog(context: Context?): AlertDialog? {
        if (context == null) return null
        val dialog: AlertDialog? = createLoadingDialog(context)
        dialog?.show()
        return dialog
    }

    fun createLoadingDialog(context: Context?): LoadingDialog? =
        if (context == null) null else LoadingDialog(context)
}

var showingDialog: Dialog? = null

fun Context?.showDialog(
    title: String? = null, message: String? = null,
    textPositive: String? = null, positiveListener: (() -> Unit)? = null,
    textNegative: String? = null, negativeListener: (() -> Unit)? = null,
    cancelable: Boolean = false, canceledOnTouchOutside: Boolean = true
): AlertDialog? {
    val context = this ?: return null
    return MaterialAlertDialogBuilder(context).apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton(textPositive) { dialog, which ->
            positiveListener?.invoke()
        }
        setNegativeButton(textNegative) { dialog, which ->
            negativeListener?.invoke()
        }
        setCancelable(cancelable)
    }.create().apply {
        setCanceledOnTouchOutside(canceledOnTouchOutside)
        if (showingDialog?.isShowing == true) {
            showingDialog?.dismiss()
        }
        if (context is LifecycleOwner) {
            context.lifecycle.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                fun onDestroy() {
                    this@apply.dismiss()
                    if (showingDialog === this@apply) {
                        showingDialog = null
                    }
                }
            })
        }
        showingDialog = this
        show()
    }
}

fun Context?.showDialog(
    title: Int? = null, message: Int? = null,
    textPositive: Int? = null, positiveListener: (() -> Unit)? = null,
    textNegative: Int? = null, negativeListener: (() -> Unit)? = null,
    cancelable: Boolean = false, canceledOnTouchOutside: Boolean = true
): AlertDialog? {
    val context = this ?: return null
    return MaterialAlertDialogBuilder(context).apply {
        if (title != null) setTitle(title)
        if (message != null) setMessage(message)
        if (textPositive != null) {
            setPositiveButton(textPositive) { dialog, which ->
                positiveListener?.invoke()
            }
        }
        if (textNegative != null) {
            setNegativeButton(textNegative) { dialog, which ->
                negativeListener?.invoke()
            }
        }
        setCancelable(cancelable)
    }.create().apply {
        setCanceledOnTouchOutside(canceledOnTouchOutside)
        if (showingDialog?.isShowing == true) {
            showingDialog?.dismiss()
        }
        if (context is LifecycleOwner) {
            context.lifecycle.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                fun onDestroy() {
                    this@apply.dismiss()
                    if (showingDialog === this@apply) {
                        showingDialog = null
                    }
                }
            })
        }
        showingDialog = this
        show()
    }
}

fun dismissShowingDialog() {
    if (showingDialog?.isShowing == true) {
        showingDialog?.dismiss()
    }
}
