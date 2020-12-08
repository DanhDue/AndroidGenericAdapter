package com.danhdueexoictif.androidgenericadapter.ui.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.danhdueexoictif.androidgenericadapter.BR
import com.danhdueexoictif.androidgenericadapter.BuildConfig
import com.danhdueexoictif.androidgenericadapter.R
import com.danhdueexoictif.androidgenericadapter.ui.widgets.ErrorDialog
import com.danhdueexoictif.androidgenericadapter.ui.widgets.LoadingDialog
import com.danhdueexoictif.androidgenericadapter.utils.DialogUtils
import com.danhdueexoictif.androidgenericadapter.utils.analytic.LoggingHelper
import com.danhdueexoictif.androidgenericadapter.utils.controller.OnEventController
import com.danhdueexoictif.androidgenericadapter.utils.extension.hideKeyBoard
import com.danhdueexoictif.androidgenericadapter.utils.showDialog
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

abstract class BaseFragment<ViewBinding : ViewDataBinding, ViewModel : BaseViewModel> :
    Fragment(), ErrorDialog.ErrorDialogListener, OnEventController {

    protected val loggingHelper: LoggingHelper by inject()

    protected open var isOnViewCreated: Boolean? = null

    var loadingDialog: LoadingDialog? = null

    protected open val navController: NavController by lazy { findNavController() }

    @get:LayoutRes
    abstract val layoutId: Int

    lateinit var viewBinding: ViewBinding

    abstract val viewModel: ViewModel

    protected val sharedViewModel: SharedViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = DataBindingUtil.inflate<ViewBinding>(inflater, layoutId, container, false).let {
        this.viewBinding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isOnViewCreated = true
        viewBinding.apply {
            setVariable(BR.viewModel, viewModel)
            root.isClickable = true
            lifecycleOwner = viewLifecycleOwner
            executePendingBindings()
            setupHideKeyboardWhenTouchOutSide(root)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeField()
        loadingDialog = DialogUtils.createLoadingDialog(requireContext())
        setupViewModels()
    }

    /**
     * setup ViewModels
     */
    private fun setupViewModels() {
        viewModel.apply {
            obsError.observe(viewLifecycleOwner, Observer {
                hideLoading()
                if (it.isNotBlank() && BuildConfig.DEBUG) {
                    handleShowErrorMessage(it)
                } else {
                    // show common error
                    showCommonError()
                }
            })
            isLoading.observe(viewLifecycleOwner, Observer { handleShowLoading(it == true) })
            noInternetConnectionEvent.observe(
                viewLifecycleOwner,
                Observer { sharedViewModel.showNoInternetDialog.call() })
            connectTimeoutEvent.observe(viewLifecycleOwner, Observer {})
            serverMaintainEvent.observe(viewLifecycleOwner, Observer {})
            showRequiredUpgradeDialog.observe(
                viewLifecycleOwner,
                Observer { sharedViewModel.showRequiredUpgradeDialog.call() })
        }
        sharedViewModel.apply {
            refreshDataIsNeeded.observe(
                viewLifecycleOwner,
                Observer { if (it == true) refreshDataIsNeeded() })
        }
    }

    private fun showCommonError() {
        Timber.d("showCommonError()")
        DialogUtils.createErrorDialog(
            requireContext(), null
        ).apply {
            onErrorDialogListener = this@BaseFragment
            show()
        }
    }

    protected open fun refreshDataIsNeeded() {}

    protected open fun resetViewsBeforeRefreshing() {}

    @SuppressLint("ClickableViewAccessibility")
    open fun setupHideKeyboardWhenTouchOutSide(view: View?) {
        lifecycleScope.launch {
            // Set up touch listener for non-text box views to hide keyboard.
            if (view !is EditText) {
                view?.setOnTouchListener { _, _ ->
                    hideKeyBoard()
                    false
                }
            }
            //If a layout container, iterate over children and seed recursion.
            if (view is ViewGroup) {
                for (i in 0 until view.childCount) {
                    val innerView = view.getChildAt(i)
                    setupHideKeyboardWhenTouchOutSide(innerView)
                }
            }
        }
    }

    open fun observeField() {}

    override fun onEvent(eventType: Int, view: View?, data: Any?) {}

    override fun errorDialogCancelButClicked() {}

    override fun errorDialogRetryButClicked() {}

    open fun handleShowLoading(isLoading: Boolean) {
        if (isLoading) showLoading() else hideLoading()
    }

    open fun handleForceUpdate() {}

    open fun handleShowErrorMessage(message: String) {
        context?.showDialog(message = message, textPositive = getString(R.string.txt_ok))
    }

    fun showLoading() {
        loadingDialog?.show()
    }

    fun hideLoading() {
        loadingDialog?.let {
            if (it.isShowing) it.dismiss()
        }
    }
}
