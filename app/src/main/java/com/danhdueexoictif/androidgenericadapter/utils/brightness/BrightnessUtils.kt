package com.danhdueexoictif.androidgenericadapter.utils.brightness

import android.content.Context
import android.os.Handler
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.view.Window
import android.view.WindowManager
import com.danhdueexoictif.androidgenericadapter.utils.brightness.BrightnessUtils.Companion.brightnessChanging
import com.danhdueexoictif.androidgenericadapter.utils.brightness.BrightnessUtils.Companion.currentAppBrightnessValue
import com.danhdueexoictif.androidgenericadapter.utils.brightness.BrightnessUtils.Companion.isMaxBrightness
import com.danhdueexoictif.androidgenericadapter.utils.brightness.BrightnessUtils.Companion.sysBrightnessValue

/**
 * An implementation of [BrightnessHelper]
 */
enum class BrightnessUtils : BrightnessHelper {
    /**
     * This one just keep BrightnessUtils/BrightnessHelper is a singleton instance.
     * A singleton instance without members. Notice:
     * Call {@link [BrightnessUtils].[Companion].[init]) } method instead.
     */
    INSTANCE;

    private var context: Context? = null

    /**
     * @return true if Device's auto brightness mode is on otherwise @return false.
     */
    override fun isAutoBrightnessMode(): Boolean {
        var brightness = 0
        context?.let {
            try {
                brightness = Settings.System.getInt(
                    it.contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE,
                    Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
                )
            } catch (e: SettingNotFoundException) {
                e.printStackTrace()
            }
        }
        return brightness != 0
    }

    /**
     * retrieve Device's system brightness value
     * @return sysBrightnessValue : The brightness value in the device's system setting.
     */
    override fun retrieveSystemBrightnessValue(): Float {
        var brightness = 0
        context?.let {
            try {
                brightness =
                    Settings.System.getInt(it.contentResolver, Settings.System.SCREEN_BRIGHTNESS)
            } catch (e: SettingNotFoundException) {
                e.printStackTrace()
            }
        }
        sysBrightnessValue = brightness / 255F
        return sysBrightnessValue
    }

    companion object {
        var sysBrightnessValue = 0F
        var currentAppBrightnessValue = 0F
        var isMaxBrightness = false
        var brightnessChanging = false

        /**
         * initialize members and retrieve singleton instance.
         */
        fun init(context: Context): BrightnessUtils {
            INSTANCE.context = context
            sysBrightnessValue = INSTANCE.retrieveSystemBrightnessValue()
            currentAppBrightnessValue = INSTANCE.retrieveSystemBrightnessValue()
            if (INSTANCE.retrieveSystemBrightnessValue() == 1F) isMaxBrightness = true
            return INSTANCE
        }
    }
}

/**
 * change brightness value for App's window
 * @param brightnessValue: the brightness value wanted to set for the App.
 */
fun Window.changeAppScreenBrightnessValue(brightnessValue: Float) {
    isMaxBrightness = brightnessValue == 1.0F
    currentAppBrightnessValue = brightnessValue
    val layoutParams = this.attributes
    layoutParams.screenBrightness = brightnessValue
    this.attributes = layoutParams
}

/**
 * restore brightness while the Auto Brightness Mode is on
 */
private fun Window.restoreBrightnessOverrideMode() {
    val layoutParams = this.attributes
    layoutParams.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
    this.attributes = layoutParams
}

/**
 * set full brightness while the Auto Brightness Mode is on
 */
private fun Window.overrideFullBrightness() {
    val layoutParams = this.attributes
    layoutParams.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL
    this.attributes = layoutParams
}

/**
 *  setup max brightness for the App.
 */
fun Window.changeMaxBrightness() {
    if (isMaxBrightness) return
    if (brightnessChanging) return

    // Change screen brightness while the Auto Brightness Mode is on
    if (BrightnessUtils.init(context).isAutoBrightnessMode()) {
        overrideFullBrightness()
        isMaxBrightness = true
        return
    }

    // Otherwise change screen brightness while the Auto Brightness Mode is off
    val handler = Handler()
    val runnable: Runnable = object : Runnable {
        override fun run() {
            currentAppBrightnessValue += 0.07f
            changeAppScreenBrightnessValue(currentAppBrightnessValue)
            if (currentAppBrightnessValue < 1.0f) {
                brightnessChanging = true
                handler.postDelayed(this, 100)
            } else {
                changeAppScreenBrightnessValue(1.0f)
                currentAppBrightnessValue = sysBrightnessValue
                isMaxBrightness = true
                brightnessChanging = false
                handler.removeCallbacks(this)
            }
        }
    }
    handler.postDelayed(runnable, 0)
}

/**
 * restore brightness follow system settings.
 */
fun Window.changeBrightnessToDefault() {
    if (brightnessChanging) return

    // Change screen brightness while the Auto Brightness Mode is on
    if (BrightnessUtils.init(context).isAutoBrightnessMode()) {
        restoreBrightnessOverrideMode()
        isMaxBrightness = false
        return
    }

    // Otherwise change screen brightness while the Auto Brightness Mode is off
    val handler = Handler()
    val runnable: Runnable = object : Runnable {
        override fun run() {
            currentAppBrightnessValue -= 0.07f
            changeAppScreenBrightnessValue(currentAppBrightnessValue)
            if (currentAppBrightnessValue > sysBrightnessValue) {
                brightnessChanging = true
                handler.postDelayed(this, 100)
            } else {
                changeAppScreenBrightnessValue(sysBrightnessValue)
                currentAppBrightnessValue = sysBrightnessValue
                brightnessChanging = false
                isMaxBrightness = false
                handler.removeCallbacks(this)
            }
        }
    }
    handler.postDelayed(runnable, 0)
    changeAppScreenBrightnessValue(sysBrightnessValue)
}
