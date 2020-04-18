package com.danhdueexoictif.androidgenericadapter.utils.locale

import android.content.Context
import android.content.res.Configuration
import com.danhdueexoictif.androidgenericadapter.data.local.pref.SharedPreferenceHelper
import java.util.*

class AppLocaleHelper(private val sharedPreferenceHelper: SharedPreferenceHelper) :
    LocaleHelper {

    override fun getLanguage(): String = getPersistedData(Locale.getDefault().language)

    override fun onAttach(context: Context): Context {
        // default language is system setting language
        val language = getPersistedData(Locale.getDefault().language)
        return getNewContext(context, language)
    }

    override fun onAttach(context: Context, language: String): Context {
        return getNewContext(context, language)
    }

    override fun setLocale(context: Context, language: String) {
        persist(language)
        updateResources(context, language)
    }

    private fun getPersistedData(defaultLanguage: String): String {
        val language = sharedPreferenceHelper.getLocalSettingLanguage(defaultLanguage)
        return if (language.isNullOrEmpty()) Locale.getDefault().language else language
    }

    override fun persist(language: String) {
        if (!language.equals(Locale.getDefault().language, true)) {
            sharedPreferenceHelper.setLocalSettingLanguage(language)
        }
    }

    @Suppress("DEPRECATION")
    private fun updateResources(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = Configuration(resources.configuration).apply {
            setLocale(locale)
            setLayoutDirection(locale)
        }
        // createConfigurationContext(configuration) and return newContext are needed for the API level 26 and newer.
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

    private fun getNewContext(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = Configuration(resources.configuration).apply {
            setLocale(locale)
            setLayoutDirection(locale)
        }
        // createConfigurationContext(configuration) and return newContext are needed for the API level 26 and newer.
        return context.createConfigurationContext(configuration)
    }
}
