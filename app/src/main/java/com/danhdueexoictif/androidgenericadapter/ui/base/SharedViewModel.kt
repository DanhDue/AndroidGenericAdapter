package com.danhdueexoictif.androidgenericadapter.ui.base

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.danhdueexoictif.androidgenericadapter.data.remote.NetworkResponse
import com.danhdueexoictif.androidgenericadapter.utils.SingleLiveEvent
import com.danhdueexoictif.androidgenericadapter.utils.controller.OnEventController.Companion.SHOW_UPGRADE_REQUIRED_DIALOG
import timber.log.Timber

class SharedViewModel : BaseViewModel() {
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

    override fun onEvent(eventType: Int, view: View?, data: Any?) {
        super.onEvent(eventType, view, data)
        when (eventType) {
            SHOW_UPGRADE_REQUIRED_DIALOG -> {
                Timber.d("SHOW_UPGRADE_REQUIRED_DIALOG")
                showUpgradeRequiredDialog.value =
                    data as NetworkResponse.ServerError<*>
            }
        }
    }
}
