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
     * Save refresh token of user.
     */
    fun setRefreshToken(refreshToken: String)

    /**
     * Get refresh token of user.
     *
     * @return: [Constants.DEF_REFRESH_TOKEN] if we don't save, otherwise refresh token.
     */
    fun getRefreshToken(): String

    /**
     * Save expire duration of token.
     */
    fun setExpiresIn(expiresIn: Long)

    /**
     * Get expire duration of token.
     *
     * @return: [Constants.DEF_REFRESH_TOKEN] if we don't save, otherwise expire duration of token.
     */
    fun getExpiresIn(): Long

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

    fun setSettingLanguage(language: String)

    fun getSettingLanguage(value: String): String

    /**
     * Clear user information
     */
    fun clearUserLocalData()
}
