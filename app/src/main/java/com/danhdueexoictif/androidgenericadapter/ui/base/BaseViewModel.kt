package com.danhdueexoictif.androidgenericadapter.ui.base

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.danhdueexoictif.androidgenericadapter.data.remote.NetworkResponse
import com.danhdueexoictif.androidgenericadapter.data.remote.NoConnectivityException
import com.danhdueexoictif.androidgenericadapter.data.remote.invoke
import com.danhdueexoictif.androidgenericadapter.data.remote.response.ErrorResponse
import com.danhdueexoictif.androidgenericadapter.data.remote.response.HttpResponseCode
import com.danhdueexoictif.androidgenericadapter.utils.SingleLiveEvent
import com.danhdueexoictif.androidgenericadapter.utils.controller.OnEventController
import com.danhdueexoictif.androidgenericadapter.utils.controller.ViewModelObservable
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class BaseViewModel : ViewModel(), OnEventController, KoinComponent {

    protected val eventController: ViewModelObservable by inject()
    val isShowShimmer = MutableLiveData(true)
    val isLoading = SingleLiveEvent<Boolean>()
    val obsError = MutableLiveData<String>()
    val serverErrorResponse = MutableLiveData<ErrorResponse>()
    val noInternetConnectionEvent = SingleLiveEvent<String>()
    val connectTimeoutEvent = SingleLiveEvent<Unit>()
    val serverMaintainEvent = SingleLiveEvent<Unit>()
    var isLoadFail = SingleLiveEvent<Boolean>().apply { value = false }

    // LiveData support to show/hide required upgrade dialog on Activity.
    val showRequiredUpgradeDialog = SingleLiveEvent<Boolean>()

    init {
        Timber.d("init()")
        eventController.register(this)
    }

    override fun onCleared() {
        Timber.d("onCleared()")
        eventController.unregister(this)
        super.onCleared()
    }

    override fun onEvent(eventType: Int, view: View?, data: Any?) {
        Timber.d("(eventType: $eventType, view: View?, data: Any?)")
    }

    protected open fun <T : Any, U : Any> NetworkResponse<T, U>.handleBaseErrors() {
        Timber.d("handleError()")
        when (this) {
            is NetworkResponse.ServerError -> {
                Timber.d("NetworkResponse.ServerError")
                if (code == HttpResponseCode.HTTP_UPGRADE_REQUIRED) {
                    showRequiredUpgradeDialog.value = true
                }
            }
            is NetworkResponse.NetworkError -> {
                Timber.d("NetworkResponse.NetworkError")
                when (this()) {
                    is SocketTimeoutException -> {
                        Timber.d("SocketTimeoutException")
                    }
                    is NoConnectivityException -> {
                        Timber.d("NoConnectivityException")
                        noInternetConnectionEvent.value = error.message
                    }
                    is UnknownHostException -> {
                        // Show common error

                    }
                    else -> {
                        // Show common error

                    }
                }
            }
        }
    }

    open fun reLoadData() {}

}
