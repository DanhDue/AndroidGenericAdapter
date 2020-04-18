package com.danhdueexoictif.androidgenericadapter.ui.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.danhdueexoictif.androidgenericadapter.data.remote.invoke
import com.danhdueexoictif.androidgenericadapter.utils.controller.OnEventController
import com.danhdueexoictif.androidgenericadapter.utils.controller.ViewObservable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.viewModel
import timber.log.Timber

abstract class BaseActivity : AppCompatActivity(), OnEventController {

    @get: LayoutRes
    abstract val layoutId: Int

    protected val sharedViewModel: SharedViewModel by viewModel()
    protected open val eventController: ViewObservable by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        eventController.register(this)
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        setupObservers()
    }

    private fun setupObservers() {
        sharedViewModel.apply {
            sharedViewModel.showUpgradeRequiredDialog.observe(this@BaseActivity, Observer { it() })
        }
    }

    override fun onDestroy() {
        eventController.unregister(this)
        super.onDestroy()
    }

    override fun onEvent(eventType: Int, view: View?, data: Any?) {}

    override fun onBooleanEvent(eventType: Int, view: View?, data: Any?): Boolean? {
        Timber.d("onBooleanEvent(eventType: Int, view: View?, data: Any?)")
        return super.onBooleanEvent(eventType, view, data)
    }
}
