package com.danhdueexoictif.androidgenericadapter.utils.locale

import android.content.Context

interface LocaleHelper {
    fun onAttach(context: Context): Context
    fun onAttach(context: Context, defaultLanguage: String): Context
    fun setLocale(context: Context, language: String)
    fun getLanguage(): String
    fun persist(language: String)
}
