package com.danhdueexoictif.androidgenericadapter.utils.theme

import android.app.Activity
import android.content.Intent
import com.danhdueexoictif.androidgenericadapter.R

enum class ThemeHelperImpl : ThemeHelper {
    INSTANCE;

    override fun changeToTheme(activity: Activity, theme: Int) {
        activity.finishAffinity()
        activity.setTheme(theme)
        activity.startActivity(Intent(activity, activity.javaClass))
    }

    override fun onActivityCreateSetTheme(activity: Activity, theme: Int) {
        when (theme) {
            ThemeHelper.DEFAULT_THEME -> activity.setTheme(R.style.DefaultLanguageTheme)
            ThemeHelper.JAPANESE_THEME -> activity.setTheme(R.style.JapaneseTheme)
            else -> activity.setTheme(R.style.DefaultLanguageTheme)
        }
    }
}
