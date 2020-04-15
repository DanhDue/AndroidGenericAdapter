package com.danhdueexoictif.androidgenericadapter.ui.screen

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.danhdueexoictif.androidgenericadapter.R
import com.danhdueexoictif.androidgenericadapter.utils.brightness.BrightnessHelper
import com.danhdueexoictif.androidgenericadapter.utils.brightness.changeAppScreenBrightnessValue
import org.koin.android.ext.android.inject
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val brightnessUtils: BrightnessHelper by inject()

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
        setContentView(R.layout.activity_main)
        observerBrightnessChange()
    }


    override fun onDestroy() {
        // unregister brightness change from system settings.
        contentResolver?.unregisterContentObserver(brightnessObserver)
        super.onDestroy()
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
