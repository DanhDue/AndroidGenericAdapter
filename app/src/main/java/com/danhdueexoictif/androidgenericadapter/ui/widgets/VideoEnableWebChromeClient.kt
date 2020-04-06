package com.danhdueexoictif.androidgenericadapter.ui.widgets

import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaPlayer.OnPreparedListener
import android.view.SurfaceView
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.widget.FrameLayout
import android.widget.VideoView
import com.danhdueexoictif.androidgenericadapter.utils.Constants

/**
 * Builds a video enabled WebChromeClient.
 * @param activityNonVideoView A View in the activity's layout that contains every other view that should be hidden when the video goes full-screen.
 * @param activityVideoView A ViewGroup in the activity's layout that will display the video. Typically you would like this to fill the whole layout.
 * @param loadingView A View to be shown while the video is loading (typically only used in API level <11). Must be already inflated and not attached to a parent view.
 * @param webView The owner VideoEnabledWebView. Passing it will enable the VideoEnabledWebChromeClient to detect the HTML5 video ended event and exit full-screen.
 * Note: The web page must only contain one video tag in order for the HTML5 video ended event to work. This could be improved if needed (see Javascript code).
 */
open class VideoEnableWebChromeClient(
    var activityNonVideoView: View? = null,
    var activityVideoView: ViewGroup? = null,
    var loadingView: View? = null,
    var webView: AdvancedWebView? = null
) : WebChromeClient(), OnPreparedListener,
    OnCompletionListener, MediaPlayer.OnErrorListener {

    interface ToggledFullscreenCallback {
        fun toggledFullscreen(fullscreen: Boolean)
    }

    /**
     * Indicates if the video is being displayed using a custom view (typically full-screen)
     * @return true it the video is being displayed using a custom view (typically full-screen)
     */
    var isVideoFullscreen = false
    private var videoViewContainer: FrameLayout? = null
    private var videoViewCallback: CustomViewCallback? = null
    private var toggledFullscreenCallback: ToggledFullscreenCallback? = null

    /**
     * Set a callback that will be fired when the video starts or finishes displaying using a custom view (typically full-screen)
     * @param callback A VideoEnabledWebChromeClient.ToggledFullscreenCallback callback
     */
    fun setOnToggledFullscreen(callback: ToggledFullscreenCallback?) {
        toggledFullscreenCallback = callback
    }

    override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
        if (view is FrameLayout) {
            // A video wants to be shown
            val focusedChild: View = view.focusedChild
            // Save video related variables
            isVideoFullscreen = true
            videoViewContainer = view
            videoViewCallback = callback
            // Hide the non-video view, add the video view, and show it
            activityNonVideoView?.visibility = INVISIBLE
            activityVideoView?.addView(
                videoViewContainer,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
            activityVideoView?.visibility = VISIBLE
            if (focusedChild is VideoView) { // android.widget.VideoView (typically API level <11)
                val videoView = focusedChild as VideoView
                // Handle all the required events
                videoView.setOnPreparedListener(this)
                videoView.setOnCompletionListener(this)
                videoView.setOnErrorListener(this)
            } else {
                // Other classes, including:
                // - android.webkit.HTML5VideoFullScreen$VideoSurfaceView, which inherits from android.view.SurfaceView (typically API level 11-18)
                // - android.webkit.HTML5VideoFullScreen$VideoTextureView, which inherits from android.view.TextureView (typically API level 11-18)
                // - com.android.org.chromium.content.browser.ContentVideoView$VideoSurfaceView, which inherits from android.view.SurfaceView (typically API level 19+)
                // Handle HTML5 video ended event only if the class is a SurfaceView
                // Test case: TextureView of Sony Xperia T API level 16 doesn't work fullscreen when loading the javascript below
                val jsIsEnable = webView?.settings?.javaScriptEnabled ?: false
                if (webView != null && jsIsEnable && focusedChild is SurfaceView) {
                    // Run javascript code that detects the video end and notifies the Javascript interface
                    var js = "javascript:"
                    js += "var _ytrp_html5_video_last;"
                    js += "var _ytrp_html5_video = document.getElementsByTagName('video')[0];"
                    js += "if (_ytrp_html5_video != undefined && _ytrp_html5_video != _ytrp_html5_video_last) {"
                    run {
                        js += "_ytrp_html5_video_last = _ytrp_html5_video;"
                        js += "function _ytrp_html5_video_ended() {"
                        run {
                            // notify video is ended to the AdvancedWebView
                            // Must match Javascript interface name and method of AdvancedWebView
                            js += "${Constants.WEB_VIEW_JAVASCRIPT_INTERFACE_NAME}.notifyVideoEnd();"
                        }
                        js += "}"
                        js += "_ytrp_html5_video.addEventListener('ended', _ytrp_html5_video_ended);"
                    }
                    js += "}"
                    webView?.loadUrl(js)
                }
            }
            // Notify full-screen change
            toggledFullscreenCallback?.toggledFullscreen(true)
        }
    }

    override fun onShowCustomView(
        view: View?,
        requestedOrientation: Int,
        callback: CustomViewCallback?
    ) // Available in API level 14+, deprecated in API level 18+
    {
        onShowCustomView(view, callback)
    }

    override fun onHideCustomView() {
        // This method should be manually called on video end in all cases because it's not always called automatically.
        // This method must be manually called on back key press (from this class' onBackPressed() method).
        if (isVideoFullscreen) { // Hide the video view, remove it, and show the non-video view
            activityVideoView?.visibility = View.INVISIBLE
            activityVideoView?.removeView(videoViewContainer)
            activityNonVideoView?.visibility = VISIBLE
            // Call back (only in API level <19, because in API level 19+ with chromium webview it crashes)
            val containChromium: Boolean =
                videoViewCallback?.javaClass?.name?.contains(".chromium.") ?: false
            if (videoViewCallback != null && !containChromium) {
                videoViewCallback?.onCustomViewHidden()
            }
            // Reset video related variables
            isVideoFullscreen = false
            videoViewContainer = null
            videoViewCallback = null
            // Notify full-screen change
            toggledFullscreenCallback?.toggledFullscreen(false)
        }
    }

    override fun getVideoLoadingProgressView(): View? {
        // Video will start loading
        return loadingView?.let {
            it.visibility = VISIBLE
            it
        } ?: super.getVideoLoadingProgressView()

    }

    override fun onPrepared(mp: MediaPlayer) {
        // Video will start playing, only called in the case of android.widget.VideoView (typically API level <11)
        loadingView?.visibility = View.GONE
    }

    override fun onCompletion(mp: MediaPlayer) {
        // Video finished playing, only called in the case of android.widget.VideoView (typically API level <11)
        onHideCustomView()
    }

    override fun onError(mp: MediaPlayer, what: Int, extra: Int): Boolean {
        // Error while playing video, only called in the case of android.widget.VideoView (typically API level <11)
        return false // By returning false, onCompletion() will be called
    }

    /**
     * Notifies the class that the back key has been pressed by the user.
     * This must be called from the Activity's onBackPressed(), and if it returns false, the activity itself should handle it. Otherwise don't do anything.
     * @return Returns true if the event was handled, and false if was not (video view is not visible)
     */
    fun onBackPressed(): Boolean {
        return if (isVideoFullscreen) {
            onHideCustomView()
            true
        } else {
            false
        }
    }
}
