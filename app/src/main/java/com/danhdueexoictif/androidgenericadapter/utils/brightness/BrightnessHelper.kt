package com.danhdueexoictif.androidgenericadapter.utils.brightness

interface BrightnessHelper {
    /**
     * @return true if Device's auto brightness mode is on otherwise @return false.
     */
    fun isAutoBrightnessMode(): Boolean

    /**
     * retrieve Device's system brightness value
     * @return sysBrightnessValue : The brightness value in the device's system setting.
     */
    fun retrieveSystemBrightnessValue(): Float
}
