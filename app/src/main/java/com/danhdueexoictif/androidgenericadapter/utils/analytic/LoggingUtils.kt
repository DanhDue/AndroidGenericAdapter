package com.danhdueexoictif.androidgenericadapter.utils.analytic

import android.app.Activity
import android.os.Bundle
import androidx.annotation.StringDef
import com.danhdueexoictif.androidgenericadapter.data.repository.UserRepository
import com.danhdueexoictif.androidgenericadapter.utils.analytic.LoggingUtils.ScreenName.Companion.HOME_SCREEN
import com.google.firebase.analytics.FirebaseAnalytics
import timber.log.Timber

class LoggingUtils(
    private val firebaseAnalytics: FirebaseAnalytics,
    private val userRepository: UserRepository
) :
    LoggingHelper {
    override fun recordFirebaseScreenView(
        activity: Activity,
        @ScreenName screenName: String?,
        newsCategory: String?
    ) {
        Timber.d("recordFbScreenView(activity: Activity?, screenName: $screenName), newsCategory: $newsCategory")
        firebaseAnalytics.setCurrentScreen(activity, screenName, newsCategory)
    }

    override fun logFirebaseEvent(
        @ScreenName screenName: String?, eventName: String,
        newsCategory: String?
    ) {
        Timber.d("logFirebaseEvent(screen: $screenName, buttonName: $eventName, event: $newsCategory)")
        val bundle = Bundle()
        bundle.putString(KEY_OS_NAME, OS_NAME_VALUE)
        bundle.putString(
            KEY_SUN_ASTERISK_MEMBER,
            SUN_ASTERISK_MEMBER_VALUE
        )
        bundle.putString(KEY_USER_NAME, "userRepository.getUserName()")
        bundle.putString(KEY_SCREEN_NAME, screenName)
        bundle.putString(KEY_NEWS_CATEGORY, newsCategory)
        firebaseAnalytics.logEvent(eventName, bundle)
    }

    override fun logEvent(
        @ScreenName screenName: String?, eventName: String,
        newsCategory: String?
    ) {
        Timber.d("logEvent(screen: $screenName, buttonName: $eventName, event: $newsCategory)")
        logFirebaseEvent(screenName, eventName, newsCategory)
    }

    companion object {
        const val KEY_OS_NAME = "OS"
        const val OS_NAME_VALUE = "Android"
        const val KEY_SUN_ASTERISK_MEMBER = "Sun_Asterisk_Member"
        const val SUN_ASTERISK_MEMBER_DEFAULT_VALUE = "NOT_ME"
        const val SUN_ASTERISK_MEMBER_VALUE = "ITS_ME"
        const val KEY_USER_NAME = "KEY_USER_NAME"
        const val KEY_SCREEN_NAME = "SCREEN_NAME"
        const val KEY_NEWS_CATEGORY = "NEWS_CATEGORY"
        const val KEY_BUTTON_CLICKED = "BUTTON_CLICKED"
        const val INIT_VALUE = "INIT"
        const val NEWS_DETAIL_INIT = "NEWS_DETAIL_INIT"
    }

    @StringDef(
        HOME_SCREEN
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class ScreenName {
        companion object {
            const val HOME_SCREEN = "HOME_SCREEN"
        }
    }
}
