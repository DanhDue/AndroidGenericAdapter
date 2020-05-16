package com.danhdueexoictif.androidgenericadapter.ui.widgets.oopsnointernet

import com.danhdueexoictif.androidgenericadapter.ui.base.BaseViewModel
import com.danhdueexoictif.androidgenericadapter.utils.SingleLiveEvent

class OopsNoInternetDialogViewModel : BaseViewModel() {
    val airPlanIsOn = SingleLiveEvent<Boolean>().apply { value = false }
}
