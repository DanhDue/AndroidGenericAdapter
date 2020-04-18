package com.danhdueexoictif.androidgenericadapter.ui.screen

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.danhdueexoictif.androidgenericadapter.R
import com.danhdueexoictif.androidgenericadapter.ui.base.BaseActivity
import com.danhdueexoictif.androidgenericadapter.utils.brightness.BrightnessHelper
import com.danhdueexoictif.androidgenericadapter.utils.brightness.changeAppScreenBrightnessValue
import org.koin.android.ext.android.inject
import timber.log.Timber

class MainActivity : BaseActivity() {

    private val brightnessUtils: BrightnessHelper by inject()

    override val layoutId: Int = R.layout.activity_main

    /**
     * This observer listens for changes in system brightness settings and then sets its value for the Application's window.
     */
    private val brightnessObserver: ContentObserver =
        object : ContentObserver(Handler()) {
            override fun onChange(selfChange: Boolean) {
                super.onChange(selfChange)
                Timber.i("mBrightnessObserver")
                // TODO: change application brightness follow the system brightness value
                window.changeAppScreenBrightnessValue(brightnessUtils.retrieveSystemBrightnessValue())
            }

            override fun deliverSelfNotifications(): Boolean {
                return true
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observerBrightnessChange()
        findViewById<ViewGroup>(R.id.drawer).systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        setupViewModels()
    }


    override fun onDestroy() {
        // unregister brightness change from system settings.
        contentResolver?.unregisterContentObserver(brightnessObserver)
        super.onDestroy()
    }

    private fun setupViewModels() {
        sharedViewModel.apply {
            // observer to show required upgrade dialog
            showRequiredUpgradeDialog.observe(this@MainActivity, Observer {
                if (it == true) Timber.d("showRequiredUpgradeDialog")
            })
        }
    }

    /**
     * observer the system brightness change.
     */
    private fun observerBrightnessChange() {
        // setup current App brightness value = brightness value from system settings.
        brightnessUtils.retrieveSystemBrightnessValue()
        // register brightness change from system settings.
        contentResolver?.registerContentObserver(
            Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS),
            false,
            brightnessObserver
        )
    }
}
