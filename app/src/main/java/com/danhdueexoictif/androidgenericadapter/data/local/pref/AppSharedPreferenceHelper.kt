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
    override fun setAccessToken(accessToken: String) =
        sharedPrefsApi.set(SharedPrefsKey.ACCESS_TOKEN, accessToken)

    /**
     * An implementation of [SharedPreferenceHelper.getAccessToken]
     */
    override fun getAccessToken(): String = sharedPrefsApi.get(
        SharedPrefsKey.ACCESS_TOKEN, Constants.DEF_ACCESS_TOKEN
    ) ?: Constants.DEF_ACCESS_TOKEN

    /**
     * return device's UUID saved on shared preference otherwise random a new one.
     */
    @Synchronized
    override fun getUUID(): String =
        sharedPrefsApi.get(SharedPrefsKey.UUID, null) ?: UUID.randomUUID().toString()
            .apply { sharedPrefsApi.set(SharedPrefsKey.UUID, this) }

    /**
     * An implementation of [SharedPreferenceHelper.setStartAppFirstTime]
     */
    override fun setStartAppFirstTime(firstTime: Boolean) =
        sharedPrefsApi.set(SharedPrefsKey.FIRST_RUN, firstTime)

    /**
     * An implementation of [SharedPreferenceHelper.isStartAppFirstTime]
     */
    override fun isStartAppFirstTime(): Boolean =
        sharedPrefsApi.get(SharedPrefsKey.FIRST_RUN, true)

    /**
     * An implementation of [SharedPreferenceHelper.setLocalSettingLanguage]
     */
    override fun setLocalSettingLanguage(language: String) =
        sharedPrefsApi.set(SharedPrefsKey.USER_LANGUAGE, language)

    /**
     * An Implementation of [SharedPreferenceHelper.getLocalSettingLanguage]
     */
    override fun getLocalSettingLanguage(value: String): String =
        sharedPrefsApi.get(SharedPrefsKey.USER_LANGUAGE, Locale.getDefault().language) ?: ""

    /**
     * An implementation of [SharedPreferenceHelper.clearUserLocalData]
     */
    override fun clearUserLocalData() = sharedPrefsApi.remove(SharedPrefsKey.FIRST_RUN)

    override fun saveUserName() = sharedPrefsApi.set(SharedPrefsKey.USER_NAME, "DanhDue ExOICTIF")

    override fun getUserName(): String? = sharedPrefsApi.get(SharedPrefsKey.USER_NAME, null)
}
