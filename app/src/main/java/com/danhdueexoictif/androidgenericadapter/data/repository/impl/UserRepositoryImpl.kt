package com.danhdueexoictif.androidgenericadapter.data.repository.impl

import com.danhdueexoictif.androidgenericadapter.data.local.pref.SharedPreferenceHelper
import com.danhdueexoictif.androidgenericadapter.data.repository.UserRepository

/**
 * An implementation of [UserRepository]
 */
class UserRepositoryImpl(private val sharedPref: SharedPreferenceHelper) : UserRepository {

    /**
     * An implementation of [UserRepository.getUUID]
     */
    override fun getUUID(): String = sharedPref.getUUID()

    /**
     * An implementation of [UserRepository.saveAppStartFirstTime]
     */
    override fun saveAppStartFirstTime(firstTime: Boolean) = sharedPref.setStartAppFirstTime(firstTime)

    /**
     * An implementation of [UserRepository.isAppStartFirstTime]
     */
    override fun isAppStartFirstTime(): Boolean = sharedPref.isStartAppFirstTime()
}
