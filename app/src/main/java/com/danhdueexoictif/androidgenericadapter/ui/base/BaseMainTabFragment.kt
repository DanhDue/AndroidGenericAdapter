package com.danhdueexoictif.androidgenericadapter.ui.base

import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.danhdueexoictif.androidgenericadapter.NavGlobalDirections
import com.danhdueexoictif.androidgenericadapter.R
import com.danhdueexoictif.androidgenericadapter.utils.Constants
import kotlin.math.absoluteValue

abstract class BaseMainTabFragment<ViewBinding : ViewDataBinding, ViewModel : BaseViewModel> :
    BaseFragment<ViewBinding, ViewModel>() {

    private var doubleBackToExitPressedOnce: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (hasOptionMenu()) {
            setHasOptionsMenu(true)
        }
    }

    open fun hasOptionMenu(): Boolean = true

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            sharedViewModel.mainTabOnBackPressSecondTime.value = System.currentTimeMillis()
        }
    }

    override fun observeField() {
        if (sharedViewModel.shouldNavigateToRoot && navController.graph.id == R.id.nav_home) {
            sharedViewModel.shouldNavigateToRoot = false
            navController.navigate(NavGlobalDirections.navigateToRoot())
        }
        sharedViewModel.apply {
            mainTabOnBackPressSecondTime.observe(viewLifecycleOwner, Observer {
                if (it - sharedViewModel.mainTabOnBackPressFirstTime < Constants.CLOSE_APP_DELAY_TIME) {
                    requireActivity().moveTaskToBack(true)
                } else {
                    Toast.makeText(
                        requireContext(),
                        getText(R.string.msg_press_back_again_to_exit),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                sharedViewModel.mainTabOnBackPressFirstTime = it.absoluteValue
            })
        }
    }
}
