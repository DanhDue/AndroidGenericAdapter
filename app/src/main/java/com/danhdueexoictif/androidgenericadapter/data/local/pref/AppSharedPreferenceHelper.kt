package com.danhdueexoictif.androidgenericadapter.data.local.pref

import com.danhdueexoictif.androidgenericadapter.utils.Constants
import java.util.*

/**
 * An implementation of [SharedPreferenceHelper]
 */
class AppSharedPreferenceHelper(private val sharedPrefsApi: SharedPrefsApi) :
    SharedPreferenceHelper {
    /**
     * An implementation of [SharedPreferenceHelper.setAccessToken]
     */
    override fun setAccessToken(accessToken: String) {
        sharedPrefsApi.set(SharedPrefsKey.ACCESS_TOKEN, accessToken)
    }

    /**
     * An implementation of [SharedPreferenceHelper.getAccessToken]
     */
    override fun getAccessToken(): String = sharedPrefsApi.get(
        SharedPrefsKey.ACCESS_TOKEN, Constants.DEF_ACCESS_TOKEN
    ) ?: Constants.DEF_ACCESS_TOKEN

    /**
     * An implementation of [SharedPreferenceHelper.setRefreshToken]
     */
    override fun setRefreshToken(refreshToken: String) {
        sharedPrefsApi.set(SharedPrefsKey.REFRESH_TOKEN, refreshToken)
    }

    /**
     * An implementation of [SharedPreferenceHelper.getRefreshToken]
     */
    override fun getRefreshToken(): String = sharedPrefsApi.get(
        SharedPrefsKey.REFRESH_TOKEN, Constants.DEF_REFRESH_TOKEN
    ) ?: Constants.DEF_REFRESH_TOKEN

    /**
     * An implementation of [SharedPreferenceHelper.setExpiresIn]
     */
    override fun setExpiresIn(expiresIn: Long) {
        sharedPrefsApi.set(SharedPrefsKey.EXPIRES_IN, expiresIn)
    }

    /**
     * An implementation of [SharedPreferenceHelper.getExpiresIn]
     */
    override fun getExpiresIn(): Long =
        sharedPrefsApi.get(SharedPrefsKey.EXPIRES_IN, Constants.DEF_EXPIRE_IN)

    /**
     * return device's UUID saved on shared preference otherwise random a new one.
     */
    @Synchronized
    override fun getUUID(): String {
        return sharedPrefsApi.get(SharedPrefsKey.UUID, null) ?: UUID.randomUUID().toString()
            .apply { sharedPrefsApi.set(SharedPrefsKey.UUID, this) }
    }

    /**
     * An implementation of [SharedPreferenceHelper.setStartAppFirstTime]
     */
    override fun setStartAppFirstTime(firstTime: Boolean) {
        sharedPrefsApi.set(SharedPrefsKey.FIRST_RUN, firstTime)
    }

    /**
     * An implementation of [SharedPreferenceHelper.isStartAppFirstTime]
     */
    override fun isStartAppFirstTime(): Boolean =
        sharedPrefsApi.get(SharedPrefsKey.FIRST_RUN, true)

    override fun setSettingLanguage(language: String) {

    }

    override fun getSettingLanguage(value: String): String {
        return ""
    }

    /**
     * An implementation of [SharedPreferenceHelper.clearUserLocalData]
     */
    override fun clearUserLocalData() {
        sharedPrefsApi.remove(SharedPrefsKey.FIRST_RUN)
    }
}
