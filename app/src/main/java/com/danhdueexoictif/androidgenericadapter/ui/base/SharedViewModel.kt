package com.danhdueexoictif.androidgenericadapter.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.danhdueexoictif.androidgenericadapter.data.remote.NetworkResponse
import com.danhdueexoictif.androidgenericadapter.utils.SingleLiveEvent

class SharedViewModel : ViewModel() {
    var shouldNavigateToRoot = false
    var showToolbarHomeIconIsNeeded: Boolean? = false
    var mainTabOnBackPressFirstTime: Long = 0L
    var showUpgradeRequiredDialog = SingleLiveEvent<NetworkResponse.ServerError<*>>()
    val mainTabOnBackPressSecondTime = SingleLiveEvent<Long>()
    val refreshDataIsNeeded = SingleLiveEvent<Boolean>()
    private val _showMenuBadge = MutableLiveData<Boolean>()
    val showMenuBadge: LiveData<Boolean> = _showMenuBadge
    fun showOrHideMenuBadge(show: Boolean) {
        _showMenuBadge.value = show
    }

    // LiveData support to enable/disable NavigationView on Activity.
    val enableDrawer = SingleLiveEvent<Boolean>()

    // LiveData support to show/hide NavigationView on Activity.
    val showDrawer = SingleLiveEvent<Boolean>()

    // LiveData support to show/hide required upgrade dialog on Activity.
    val showRequiredUpgradeDialog = SingleLiveEvent<Boolean>()
}
