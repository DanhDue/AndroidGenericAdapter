package com.danhdueexoictif.androidgenericadapter.data.repository

/**
 * Repository define methods that relate to user.
 */
interface UserRepository {
    /**
     * Retrieve the device's UUID
     */
    fun getUUID(): String

    /**
     * Save when app starts for first time
     */
    fun saveAppStartFirstTime(firstTime: Boolean)

    /**
     * Check app start first time or not.
     *
     * @return: true if app starts for first time, otherwise false.
     */
    fun isAppStartFirstTime(): Boolean
}
