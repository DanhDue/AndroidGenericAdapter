package com.danhdueexoictif.androidgenericadapter.ui.base

import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window.FEATURE_NO_TITLE
import android.view.WindowManager.LayoutParams.*
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.danhdueexoictif.androidgenericadapter.utils.analytic.LoggingHelper
import com.danhdueexoictif.androidgenericadapter.utils.controller.OnEventController
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import com.danhdueexoictif.androidgenericadapter.BR

abstract class BaseDialogFragment<ViewBinding : ViewDataBinding, ViewModel : BaseViewModel> :
    DialogFragment(), OnEventController {

    lateinit var viewBinding: ViewBinding

    abstract val viewModel: ViewModel

    @get:LayoutRes
    abstract val layoutId: Int

    protected val sharedViewModel: SharedViewModel by sharedViewModel()

    protected val loggingHelper: LoggingHelper by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return DataBindingUtil.inflate<ViewBinding>(inflater, layoutId, container, false).let {
            this.viewBinding = it
            // retrieve display dimensions
            val displayRectangle = Rect()
            dialog?.window?.apply {
                requestFeature(FEATURE_NO_TITLE)
                decorView.getWindowVisibleDisplayFrame(displayRectangle)
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setLayout(MATCH_PARENT, MATCH_PARENT)
                addFlags(FLAG_NOT_TOUCH_MODAL)
            }
            dialog?.setCanceledOnTouchOutside(true)
            // inflate and adjust layout
            it.root.apply {
                minimumWidth = MATCH_PARENT
                minimumHeight = WRAP_CONTENT
            }
            it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.apply {
            setVariable(BR.viewModel, viewModel)
            root.isClickable = true
            lifecycleOwner = viewLifecycleOwner
            executePendingBindings()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Hide title of the dialog
        setStyle(STYLE_NO_FRAME, 0)
        observeField()
    }

    open fun observeField() {}

    override fun onEvent(eventType: Int, view: View?, data: Any?) {}
}
