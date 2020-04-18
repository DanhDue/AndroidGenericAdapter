package com.danhdueexoictif.androidgenericadapter.data.local.pref

import com.danhdueexoictif.androidgenericadapter.utils.Constants

/**
 * Interface define methods that work with SharedPreference
 */
interface SharedPreferenceHelper {

    /**
     * Save access token of user.
     */
    fun setAccessToken(accessToken: String)

    /**
     * Get access token of user.
     *
     * @return: [Constants.DEF_ACCESS_TOKEN] if we don't save, otherwise access token.
     */
    fun getAccessToken(): String

    /**
     * Save starting state of our app.
     * If app starts for the first time, we save true value. Otherwise, we save false value.
     */
    fun setStartAppFirstTime(firstTime: Boolean)

    /**
     * Get starting state of our app.
     *
     * @return: true if app starts for the first else, otherwise false.
     */
    fun isStartAppFirstTime(): Boolean

    /**
     * retrieve the device's UUID
     */
    fun getUUID(): String

    fun setLocalSettingLanguage(language: String)

    fun getLocalSettingLanguage(value: String): String

    /**
     * Clear user information
     */
    fun clearUserLocalData()
}
