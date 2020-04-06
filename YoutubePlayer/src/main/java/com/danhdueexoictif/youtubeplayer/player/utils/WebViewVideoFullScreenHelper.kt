package com.danhdueexoictif.youtubeplayer.player.utils

import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.View

/**
 * Class responsible for changing the WebView Video Player View from full screen to non-full screen and vice versa.
 * @param activity
 * @param views to hide/show
 */
class WebViewVideoFullScreenHelper(val activity: Activity, vararg views: View) {

    private val views = views.asList()

    /**
     * call this method to enter full screen
     */
    fun enterFullScreen() {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        val decorView = activity.window.decorView
        hideSystemUi(decorView)
        for (view in views) {
            view.visibility = View.GONE
            view.invalidate()
        }
    }

    /**
     * call this method to exit full screen
     */
    fun exitFullScreen() {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val decorView = activity.window.decorView
        showSystemUi(decorView)
        for (view in views) {
            view.visibility = View.VISIBLE
            view.invalidate()
        }
    }

    private fun hideSystemUi(mDecorView: View) {
        mDecorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    private fun showSystemUi(mDecorView: View) {
        mDecorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }
}
