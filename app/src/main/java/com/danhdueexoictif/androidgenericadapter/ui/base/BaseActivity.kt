package com.danhdueexoictif.androidgenericadapter.ui.base

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.danhdueexoictif.androidgenericadapter.data.remote.invoke
import com.danhdueexoictif.androidgenericadapter.utils.controller.OnEventController
import com.danhdueexoictif.androidgenericadapter.utils.controller.ViewObservable
import com.danhdueexoictif.androidgenericadapter.utils.locale.LocaleHelper
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.viewModel
import timber.log.Timber
import java.util.*

abstract class BaseActivity : AppCompatActivity(), OnEventController {

    @get: LayoutRes
    abstract val layoutId: Int

    private val localHelper: LocaleHelper? by inject()
    protected val sharedViewModel: SharedViewModel by viewModel()
    protected open val eventController: ViewObservable by inject()

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(localHelper?.onAttach(newBase))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        localHelper?.setLocale(this, localHelper?.getLanguage() ?: Locale.getDefault().language)
        super.onConfigurationChanged(newConfig)
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        val locale = Locale(localHelper?.getLanguage() ?: Locale.getDefault().language)
        Locale.setDefault(locale)
        overrideConfiguration.also {
            it?.setLocale(locale)
            it?.setLayoutDirection(locale)
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

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
