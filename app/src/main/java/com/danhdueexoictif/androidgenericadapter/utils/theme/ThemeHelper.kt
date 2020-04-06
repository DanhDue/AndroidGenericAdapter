package com.danhdueexoictif.androidgenericadapter.utils.theme

import android.app.Activity
import androidx.annotation.IntDef

interface ThemeHelper {

    fun changeToTheme(activity: Activity, @ThemeDef theme: Int)

    fun onActivityCreateSetTheme(activity: Activity, @ThemeDef theme: Int)

    companion object {
        const val DEFAULT_THEME = 0
        const val JAPANESE_THEME = 1

        @IntDef(
            DEFAULT_THEME,
            JAPANESE_THEME
        )
        @Retention(AnnotationRetention.SOURCE)
        annotation class ThemeDef
    }
}
