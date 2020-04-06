package com.danhdueexoictif.androidgenericadapter.utils.analytic

import android.app.Activity

interface LoggingHelper {
    fun recordFirebaseScreenView(activity: Activity, screenName: String?, newsCategory: String?)
    fun logFirebaseEvent(screenName: String?, eventName: String, newsCategory: String?)
    fun logEvent(screenName: String?, eventName: String, newsCategory: String?)
}
