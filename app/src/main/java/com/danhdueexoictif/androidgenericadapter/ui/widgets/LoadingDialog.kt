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
import com.danhdueexoictif.androidgenericadapter.utils.Constants
import timber.log.Timber

class LoadingDialog : AlertDialog {

    var rootView: View? = null
    private var showedAt = 0L
    var lifecycleTime = 0L

    constructor(context: Context) : super(context)

    constructor(context: Context, @StyleRes themeResId: Int) : super(context, themeResId)

    override fun onCreate(savedInstanceState: Bundle?) {
        rootView = LayoutInflater.from(context).inflate(R.layout.layout_loading_dialog, null)
        setView(rootView)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        super.onCreate(savedInstanceState)
    }

    override fun show() {
        super.show()
        showedAt = System.currentTimeMillis()
        Timber.d("showedAt - ${System.currentTimeMillis()}")
    }

    override fun onStart() {
        super.onStart()
        val animation = AnimationUtils.loadAnimation(context, R.anim.anim_loading_show)
        rootView?.visibility = View.VISIBLE
        rootView?.startAnimation(animation)
    }

    override fun dismiss() {
        val animation = AnimationUtils.loadAnimation(context, R.anim.anim_loading_dismiss)
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
        lifecycleTime =
            showedAt + Constants.LOADING_DIALOG_DISMISS_DELAY_TIME - System.currentTimeMillis()
        if (lifecycleTime < 0) {
            dismiss()
        } else {
            Handler().postDelayed({ super.dismiss() }, lifecycleTime)
        }
    }

}
