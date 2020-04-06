package com.danhdueexoictif.androidgenericadapter.ui.widgets

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog
import com.danhdueexoictif.androidgenericadapter.R
import com.danhdueexoictif.androidgenericadapter.utils.ErrorState
import com.danhdueexoictif.androidgenericadapter.utils.setClickSafe
import kotlinx.android.synthetic.main.layout_error_dialog.*
import timber.log.Timber

class ErrorDialog : AlertDialog {

    var onErrorDialogListener: ErrorDialogListener? = null

    var rootView: View? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, @StyleRes themeResId: Int) : super(context, themeResId)

    var errorState: ErrorState? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        rootView = LayoutInflater.from(context).inflate(R.layout.layout_error_dialog, null)
        setView(rootView)
        errorState?.let {
            tvTitle?.text = it.title
            tvMessage?.text = it.message
        } ?: showCommonError()
        butCancel.setOnClickListener {
            //globalEventController.postMessage(ERROR_DIALOG_CANCEL, null, null)
            onErrorDialogListener?.errorDialogCancelButClicked()
            dismiss()
        }

        butRetry.setClickSafe(View.OnClickListener {
            onErrorDialogListener?.errorDialogRetryButClicked()
            dismiss()
        })

        setCancelable(false)
        setCanceledOnTouchOutside(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        super.onCreate(savedInstanceState)
    }

    private fun showCommonError() {
        Timber.d("showCommonError()")
        tvTitle?.text = context.getString(R.string.txt_an_error_occurred)
        tvMessage?.text = context.getString(R.string.txt_common_error_message)
    }

    override fun onStart() {
        super.onStart()
        val animation = AnimationUtils.loadAnimation(context, R.anim.anim_error_dialog_show)
        rootView?.visibility = View.VISIBLE
        rootView?.startAnimation(animation)
    }

    override fun dismiss() {
        val animation = AnimationUtils.loadAnimation(context, R.anim.anim_error_dialog_dismiss)
            .apply {
                setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        rootView?.visibility = View.GONE
                    }

                    override fun onAnimationStart(animation: Animation?) {
                    }
                })
            }
        rootView?.startAnimation(animation)
        Handler().postDelayed({ super.dismiss() }, 300)
    }

    interface ErrorDialogListener {
        fun errorDialogCancelButClicked()
        fun errorDialogRetryButClicked()
    }

}
