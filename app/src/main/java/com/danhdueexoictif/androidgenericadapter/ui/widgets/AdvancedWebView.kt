package com.danhdueexoictif.androidgenericadapter.ui.widgets

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.DownloadManager
import android.app.DownloadManager.Request
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.*
import android.util.AttributeSet
import android.util.Base64
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.webkit.GeolocationPermissions.Callback
import android.webkit.WebStorage.QuotaUpdater
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.danhdueexoictif.androidgenericadapter.utils.Constants
import com.danhdueexoictif.androidgenericadapter.utils.extension.getHtmlContent
import timber.log.Timber
import java.io.UnsupportedEncodingException
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.HashMap

/** Advanced WebView component for Android that works as intended out of the box  */
@Suppress("DEPRECATION")
class AdvancedWebView : WebView {
    private var activity: WeakReference<Activity>? = null
    private var fragment: WeakReference<Fragment>? = null
    private var listener: Listener? = null
    private val lstPermittedHostNames: MutableList<String> = LinkedList()

    /** File upload callback for platform versions prior to Android 5.0  */
    private var fileUploadCallbackFirst: ValueCallback<Uri>? = null

    /** File upload callback for Android 5.0+  */
    private var fileUploadCallbackSecond: ValueCallback<Array<Uri>>? = null
    private var lastError: Long = 0
    private var languageIso3: String? = null
    private var requestCodeFilePicker = REQUEST_CODE_FILE_PICKER
    private var customWebViewClient: WebViewClient? = null
    private var customWebChromeClient: VideoEnableWebChromeClient? = null
    private var enabledGeolocation: Boolean = false
    private var uploadFileTypes = "*/*"
    private val httpHeaders: MutableMap<String, String> = HashMap()

    val permittedHostNames: List<String>
        get() = lstPermittedHostNames

    private var addedJavascriptInterface = false

    /**
     * Indicates if the video is being displayed using a custom view (typically full-screen)
     * @return true it the video is being displayed using a custom view (typically full-screen)
     */
    var isVideoFullScreen: Boolean = false
        get() = customWebChromeClient?.isVideoFullscreen ?: false

    /**
     * Provides localizations for the 25 most widely spoken languages that have a ISO 639-2/T code
     *
     * @return the label for the file upload prompts as a string
     */
    private val fileUploadPromptLabel: String
        get() {
            try {
                when (languageIso3) {
                    "zho" -> return decodeBase64("6YCJ5oup5LiA5Liq5paH5Lu2")
                    "spa" -> return decodeBase64("RWxpamEgdW4gYXJjaGl2bw==")
                    "hin" -> return decodeBase64("4KSP4KSVIOCkq+CkvOCkvuCkh+CksiDgpJrgpYHgpKjgpYfgpII=")
                    "ben" -> return decodeBase64("4KaP4KaV4Kaf4Ka/IOCmq+CmvuCmh+CmsiDgpqjgpr/gprDgp43gpqzgpr7gpprgpqg=")
                    "ara" -> return decodeBase64("2KfYrtiq2YrYp9ixINmF2YTZgSDZiNin2K3Yrw==")
                    "por" -> return decodeBase64("RXNjb2xoYSB1bSBhcnF1aXZv")
                    "rus" -> return decodeBase64("0JLRi9Cx0LXRgNC40YLQtSDQvtC00LjQvSDRhNCw0LnQuw==")
                    "jpn" -> return decodeBase64("MeODleOCoeOCpOODq+OCkumBuOaKnuOBl+OBpuOBj+OBoOOBleOBhA==")
                    "pan" -> return decodeBase64("4KiH4Kmx4KiVIOCoq+CovuCoh+CosiDgqJrgqYHgqKPgqYs=")
                    "deu" -> return decodeBase64("V8OkaGxlIGVpbmUgRGF0ZWk=")
                    "jav" -> return decodeBase64("UGlsaWggc2lqaSBiZXJrYXM=")
                    "msa" -> return decodeBase64("UGlsaWggc2F0dSBmYWls")
                    "tel" -> return decodeBase64("4LCS4LCVIOCwq+CxhuCxluCwsuCxjeCwqOCxgSDgsI7gsILgsJrgsYHgsJXgsYvgsILgsKHgsL8=")
                    "vie" -> return decodeBase64("Q2jhu41uIG3hu5l0IHThuq1wIHRpbg==")
                    "kor" -> return decodeBase64("7ZWY64KY7J2YIO2MjOydvOydhCDshKDtg50=")
                    "fra" -> return decodeBase64("Q2hvaXNpc3NleiB1biBmaWNoaWVy")
                    "mar" -> return decodeBase64("4KSr4KS+4KSH4KSyIOCkqOCkv+CkteCkoeCkvg==")
                    "tam" -> return decodeBase64("4K6S4K6w4K+BIOCuleCvh+CuvuCuquCvjeCuquCviCDgrqTgr4fgrrDgr43grrXgr4E=")
                    "urd" -> return decodeBase64("2KfbjNqpINmB2KfYptmEINmF24zauiDYs9uSINin2YbYqtiu2KfYqCDaqdix24zaug==")
                    "fas" -> return decodeBase64("2LHYpyDYp9mG2KrYrtin2Kgg2qnZhtuM2K8g24zaqSDZgdin24zZhA==")
                    "tur" -> return decodeBase64("QmlyIGRvc3lhIHNlw6dpbg==")
                    "ita" -> return decodeBase64("U2NlZ2xpIHVuIGZpbGU=")
                    "tha" -> return decodeBase64("4LmA4Lil4Li34Lit4LiB4LmE4Lif4Lil4LmM4Lir4LiZ4Li24LmI4LiH")
                    "guj" -> return decodeBase64("4KqP4KqVIOCqq+CqvuCqh+CqsuCqqOCrhyDgqqrgqrjgqoLgqqY=")
                }
            } catch (ignored: Exception) {
                ignored.printStackTrace()
            }
            // return English translation by default
            return "Choose a file"
        }

    interface Listener {
        fun onPageStarted(url: String?, favicon: Bitmap?)
        fun onPageFinished(url: String?)
        fun onPageError(errorCode: Int, description: String?, failingUrl: String?)
        fun onDownloadRequested(
            url: String?,
            suggestedFilename: String?,
            mimeType: String?,
            contentLength: Long?,
            contentDisposition: String?,
            userAgent: String?
        )

        fun onExternalPageRequest(url: String?)
    }

    constructor(context: Context) : super(context.applicationContext) {
        init(context.applicationContext)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context.applicationContext, attrs) {
        init(context.applicationContext)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context.applicationContext,
        attrs,
        defStyleAttr
    ) {
        init(context.applicationContext)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context.applicationContext, attrs, defStyleAttr, defStyleRes
    ) {
        init(context.applicationContext)
    }

    public override fun onSaveInstanceState(): Parcelable? {
        Timber.d("onSaveInstanceState()")
        return super.onSaveInstanceState()
    }

    @JvmOverloads
    fun setListener(
        activity: Activity?,
        listener: Listener,
        requestCodeFilePicker: Int = REQUEST_CODE_FILE_PICKER
    ) {
        if (activity != null) {
            this.activity = WeakReference(activity)
        } else {
            this.activity = null
        }
        setListener(listener, requestCodeFilePicker)
    }

    @JvmOverloads
    fun setListener(
        fragment: Fragment?,
        listener: Listener,
        requestCodeFilePicker: Int = REQUEST_CODE_FILE_PICKER
    ) {
        if (fragment != null) {
            this.fragment = WeakReference(fragment)
        } else {
            this.fragment = null
        }
        setListener(listener, requestCodeFilePicker)
    }

    private fun setListener(listener: Listener, requestCodeFilePicker: Int) {
        this.listener = listener
        this.requestCodeFilePicker = requestCodeFilePicker
    }

    override fun setWebViewClient(client: WebViewClient) {
        super.setWebViewClient(client)
        customWebViewClient = client
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun setWebChromeClient(client: WebChromeClient?) {
        settings.javaScriptEnabled = true
        customWebChromeClient = client as VideoEnableWebChromeClient?
        super.setWebChromeClient(client)
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun setGeolocationEnabled(enabled: Boolean) {
        if (enabled) {
            settings.javaScriptEnabled = true
            settings.setGeolocationEnabled(true)
            setGeolocationDatabasePath()
        }
        enabledGeolocation = enabled
    }

    @SuppressLint("NewApi")
    private fun setGeolocationDatabasePath() {
        fragment?.get()?.let {
            settings.setGeolocationDatabasePath(it.requireActivity().filesDir.path)
        } ?: activity?.get()?.let {
            settings.setGeolocationDatabasePath(it.filesDir.path)
        }
    }

    fun setUploadableFileTypes(mimeType: String) {
        uploadFileTypes = mimeType
    }

    /**
     * Loads and displays the provided HTML source text
     *
     * @param html the HTML source text to load
     * @param baseUrl the URL to use as the page's base URL
     * @param historyUrl the URL to use for the page's history entry
     * @param encoding the encoding or charset of the HTML source text
     */
    fun loadHtml(
        html: String,
        baseUrl: String? = null,
        historyUrl: String? = null,
        encoding: String? = "utf-8"
    ) {
        addJavascriptInterface()
        loadDataWithBaseURL(
            baseUrl,
            html.getHtmlContent() ?: "about:blank",
            "text/html",
            encoding,
            historyUrl
        )
    }

    @SuppressLint("NewApi")
    override fun onResume() {
        super.onResume()
        resumeTimers()
    }

    @SuppressLint("NewApi")
    override fun onPause() {
        pauseTimers()
        super.onPause()
    }

    fun onDestroy() {
        // try to remove this view from its parent first
        try {
            (parent as ViewGroup).removeView(this)
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }

        // then try to remove all child views from this view
        try {
            removeAllViews()
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
        // and finally destroy this view
        destroy()
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode == requestCodeFilePicker) {
            if (resultCode == Activity.RESULT_OK) {
                intent?.let { intentTmp ->
                    fileUploadCallbackFirst?.let {
                        it.onReceiveValue(intentTmp.data)
                        fileUploadCallbackFirst = null
                    } ?: fileUploadCallbackSecond?.let { fileUploadCallbackSecondTmp ->
                        var dataUris: Array<Uri>? = null
                        intentTmp.dataString?.let {
                            dataUris = arrayOf(Uri.parse(it))
                        } ?: intentTmp.clipData?.let { clipData ->
                            dataUris = arrayOf()
                            dataUris?.let {
                                for (i in 0 until clipData.itemCount) {
                                    it[i] = clipData.getItemAt(i).uri
                                }
                            }
                        }
                        dataUris?.let {
                            fileUploadCallbackSecondTmp.onReceiveValue(it)
                            this.fileUploadCallbackSecond = null
                        }
                    }
                }

            } else {
                fileUploadCallbackFirst?.let {
                    it.onReceiveValue(null)
                    fileUploadCallbackFirst = null
                } ?: fileUploadCallbackSecond?.let {
                    it.onReceiveValue(null)
                    fileUploadCallbackSecond = null
                }
            }
        }
    }

    /**
     * Adds an additional HTTP header that will be sent along with every HTTP `GET` request
     *
     * This does only affect the main requests, not the requests to included resources (e.g. images)
     *
     * If you later want to delete an HTTP header that was previously added this way, call `removeHttpHeader()`
     *
     * The `WebView` implementation may in some cases overwrite headers that you set or unset
     *
     * @param name the name of the HTTP header to add
     * @param value the value of the HTTP header to send
     */
    fun addHttpHeader(name: String, value: String) {
        httpHeaders[name] = value
    }

    /**
     * Removes one of the HTTP headers that have previously been added via `addHttpHeader()`
     *
     * If you want to unset a pre-defined header, set it to an empty string with `addHttpHeader()` instead
     *
     * The `WebView` implementation may in some cases overwrite headers that you set or unset
     *
     * @param name the name of the HTTP header to remove
     */
    fun removeHttpHeader(name: String) {
        httpHeaders.remove(name)
    }

    fun addPermittedHostname(hostname: String) {
        lstPermittedHostNames.add(hostname)
    }

    fun addPermittedHostnames(collection: Collection<String>) {
        lstPermittedHostNames.addAll(collection)
    }

    fun removePermittedHostname(hostname: String) {
        lstPermittedHostNames.remove(hostname)
    }

    fun clearPermittedHostnames() {
        lstPermittedHostNames.clear()
    }

    fun onBackPressed(): Boolean {
        return if (canGoBack()) {
            goBack()
            false
        } else {
            true
        }
    }

    fun setCookiesEnabled(enabled: Boolean) {
        CookieManager.getInstance().setAcceptCookie(enabled)
    }

    @SuppressLint("NewApi")
    fun setThirdPartyCookiesEnabled(enabled: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, enabled)
        }
    }

    fun setMixedContentAllowed(allowed: Boolean) {
        setMixedContentAllowed(settings, allowed)
    }

    @SuppressLint("NewApi")
    private fun setMixedContentAllowed(webSettings: WebSettings, allowed: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.mixedContentMode =
                if (allowed) WebSettings.MIXED_CONTENT_ALWAYS_ALLOW else WebSettings.MIXED_CONTENT_NEVER_ALLOW
        }
    }

    fun setDesktopMode(enabled: Boolean) {
        Timber.d("setDesktopMode(enabled: %b)", enabled)
        val webSettings = settings
        val newUserAgent: String
        newUserAgent = if (enabled) {
            webSettings.userAgentString.replace("Mobile", "eliboM")
                .replace("Android", "diordnA")
        } else {
            webSettings.userAgentString.replace("eliboM", "Mobile")
                .replace("diordnA", "Android")
        }

        webSettings.userAgentString = newUserAgent
        webSettings.useWideViewPort = enabled
        webSettings.loadWithOverviewMode = enabled
        webSettings.builtInZoomControls = enabled
        webSettings.setSupportZoom(enabled)
    }

    @SuppressLint("SetJavaScriptEnabled", "ObsoleteSdkInt")
    private fun init(context: Context) {
        // in IDE's preview mode
        if (isInEditMode) {
            // do not run the code from this method
            return
        }

        if (context is Activity) {
            activity = WeakReference(context)
        }

        languageIso3 = languageIso3
        isFocusable = true
        isFocusableInTouchMode = true
        isSaveEnabled = true

        val filesDir = context.filesDir.path
        val databaseDir = filesDir.substring(0, filesDir.lastIndexOf("/")) + DATABASES_SUB_FOLDER

        val webSettings = settings
        webSettings.allowFileAccess = false
        setAllowAccessFromFileUrls(webSettings, false)
        webSettings.builtInZoomControls = false
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.defaultZoom = WebSettings.ZoomDensity.FAR
        webSettings.loadWithOverviewMode = true
        webSettings.setSupportZoom(false)
        webSettings.displayZoomControls = false
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH)
        }
        webSettings.databaseEnabled = true
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            webSettings.databasePath = databaseDir
        }
        scrollBarStyle = SCROLLBARS_INSIDE_OVERLAY
        setLayerType(View.LAYER_TYPE_HARDWARE, null)

        setMixedContentAllowed(webSettings, true)

        setThirdPartyCookiesEnabled(true)
        super.setWebViewClient(object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                if (!hasError()) {
                    listener?.onPageStarted(url, favicon)
                }
                customWebViewClient?.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                if (!hasError()) {
                    listener?.onPageFinished(url)
                }
                customWebViewClient?.onPageFinished(view, url)
            }

            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                setLastError()
                listener?.onPageError(errorCode, description, failingUrl)
                customWebViewClient?.onReceivedError(view, errorCode, description, failingUrl)
            }

            @RequiresApi(Build.VERSION_CODES.N)
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                if (!isPermittedUrl(url)) {
                    // if a listener is available then inform the listener about the request
                    listener?.onExternalPageRequest(url)
                    // cancel the original request
                    return true
                }

                // if there is a user-specified handler available
                customWebViewClient?.let {
                    // if the user-specified handler asks to override the request
                    if (it.shouldOverrideUrlLoading(view, request)) {
                        // cancel the original request
                        return true
                    }
                }

                val uri = Uri.parse(url)
                val scheme = uri.scheme

                if (scheme != null) {
                    val externalSchemeIntent: Intent?
                    when (scheme) {
                        "tel" -> externalSchemeIntent = Intent(Intent.ACTION_DIAL, uri)
                        "sms" -> externalSchemeIntent = Intent(Intent.ACTION_SENDTO, uri)
                        "mailto" -> externalSchemeIntent = Intent(Intent.ACTION_SENDTO, uri)
                        "whatsapp" -> {
                            externalSchemeIntent = Intent(Intent.ACTION_SENDTO, uri)
                            externalSchemeIntent.setPackage("com.whatsapp")
                        }
                        else -> externalSchemeIntent = null
                    }

                    externalSchemeIntent?.let {
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        try {
                            activity?.get()?.startActivity(it)
                                ?: getContext().startActivity(it)
                        } catch (ignored: ActivityNotFoundException) {
                            ignored.printStackTrace()
                        }
                        // cancel the original request
                        return true
                    }
                }

                // route the request through the custom URL loading method
                view?.loadUrl(url ?: "")

                // cancel the original request
                return true
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (!isPermittedUrl(url)) {
                    // if a listener is available then inform the listener about the request
                    listener?.onExternalPageRequest(url)
                    // cancel the original request
                    return true
                }

                // if there is a user-specified handler available
                customWebViewClient?.let {
                    // if the user-specified handler asks to override the request
                    if (it.shouldOverrideUrlLoading(view, url)) {
                        // cancel the original request
                        return true
                    }
                }

                val uri = Uri.parse(url)
                val scheme = uri.scheme

                if (scheme != null) {
                    val externalSchemeIntent: Intent?
                    when (scheme) {
                        "tel" -> externalSchemeIntent = Intent(Intent.ACTION_DIAL, uri)
                        "sms" -> externalSchemeIntent = Intent(Intent.ACTION_SENDTO, uri)
                        "mailto" -> externalSchemeIntent = Intent(Intent.ACTION_SENDTO, uri)
                        "whatsapp" -> {
                            externalSchemeIntent = Intent(Intent.ACTION_SENDTO, uri)
                            externalSchemeIntent.setPackage("com.whatsapp")
                        }
                        else -> externalSchemeIntent = null
                    }

                    externalSchemeIntent?.let {
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        try {
                            activity?.get()?.startActivity(it)
                                ?: getContext().startActivity(it)
                        } catch (ignored: ActivityNotFoundException) {
                            ignored.printStackTrace()
                        }
                        // cancel the original request
                        return true
                    }
                }

                // route the request through the custom URL loading method
                view?.loadUrl(url ?: "")

                // cancel the original request
                return true
            }

            override fun onLoadResource(view: WebView?, url: String?) {
                customWebViewClient?.onLoadResource(view, url)
                    ?: super.onLoadResource(view, url)
            }

            @SuppressLint("NewApi")
            override fun shouldInterceptRequest(
                view: WebView?,
                url: String?
            ): WebResourceResponse? {
                return customWebViewClient?.shouldInterceptRequest(view, url)
                    ?: super.shouldInterceptRequest(view, url)
            }

            @SuppressLint("NewApi")
            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    customWebViewClient?.shouldInterceptRequest(view, request)
                        ?: super.shouldInterceptRequest(view, request)
                } else {
                    null
                }
            }

            override fun onFormResubmission(
                view: WebView?,
                dontResend: Message?,
                resend: Message?
            ) {
                customWebViewClient?.onFormResubmission(view, dontResend, resend)
                    ?: super.onFormResubmission(view, dontResend, resend)
            }

            override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
                customWebViewClient?.doUpdateVisitedHistory(view, url, isReload)
                    ?: super.doUpdateVisitedHistory(view, url, isReload)
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                customWebViewClient?.onReceivedSslError(view, handler, error)
                    ?: super.onReceivedSslError(view, handler, error)
            }

            @SuppressLint("NewApi")
            override fun onReceivedClientCertRequest(view: WebView?, request: ClientCertRequest?) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    customWebViewClient?.onReceivedClientCertRequest(view, request)
                        ?: super.onReceivedClientCertRequest(view, request)
                }
            }

            override fun onReceivedHttpAuthRequest(
                view: WebView?,
                handler: HttpAuthHandler?,
                host: String?,
                realm: String?
            ) {
                customWebViewClient?.onReceivedHttpAuthRequest(view, handler, host, realm)
                    ?: super.onReceivedHttpAuthRequest(view, handler, host, realm)
            }

            override fun shouldOverrideKeyEvent(view: WebView?, event: KeyEvent?): Boolean {
                return customWebViewClient?.shouldOverrideKeyEvent(view, event)
                    ?: super.shouldOverrideKeyEvent(view, event)
            }

            override fun onUnhandledKeyEvent(view: WebView?, event: KeyEvent?) {
                customWebViewClient?.onUnhandledKeyEvent(view, event)
                    ?: super.onUnhandledKeyEvent(view, event)
            }

//            override fun onUnhandledInputEvent(view: WebView?, event: InputEvent?) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    customWebViewClient?.onUnhandledInputEvent(view, event)
//                        ?: super.onUnhandledInputEvent(view, event)
//                }
//            }

            override fun onScaleChanged(view: WebView?, oldScale: Float, newScale: Float) {
                customWebViewClient?.onScaleChanged(view, oldScale, newScale)
                    ?: super.onScaleChanged(view, oldScale, newScale)
            }

            @SuppressLint("NewApi")
            override fun onReceivedLoginRequest(
                view: WebView?,
                realm: String?,
                account: String?,
                args: String?
            ) {
                customWebViewClient?.onReceivedLoginRequest(view, realm, account, args)
                    ?: super.onReceivedLoginRequest(view, realm, account, args)
            }

        })

        super.setWebChromeClient(object : WebChromeClient() {

            // file upload callback (Android 2.2 (API level 8) -- Android 2.3 (API level 10)) (hidden method)
            fun openFileChooser(uploadMsg: ValueCallback<Uri>) {
                openFileChooser(uploadMsg)
            }

            // file upload callback (Android 3.0 (API level 11) -- Android 4.0 (API level 15)) (hidden method)
            fun openFileChooser(uploadMsg: ValueCallback<Uri>, acceptType: String?) {
                openFileChooser(uploadMsg, acceptType)
            }

            // file upload callback (Android 4.1 (API level 16) -- Android 4.3 (API level 18)) (hidden method)
            fun openFileChooser(
                uploadMsg: ValueCallback<Uri>,
                acceptType: String?,
                capture: String?
            ) {
                openFileInput(uploadMsg, null, false)
            }

            // file upload callback (Android 5.0 (API level 21) -- current) (public method)
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val allowMultiple =
                        fileChooserParams?.mode == FileChooserParams.MODE_OPEN_MULTIPLE
                    openFileInput(null, filePathCallback, allowMultiple)
                    true
                } else {
                    false
                }
            }

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                customWebChromeClient?.onProgressChanged(view, newProgress)
                    ?: super.onProgressChanged(view, newProgress)
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                customWebChromeClient?.onReceivedTitle(view, title)
                    ?: super.onReceivedTitle(view, title)
            }

            override fun onReceivedIcon(view: WebView?, icon: Bitmap?) {
                customWebChromeClient?.onReceivedIcon(view, icon)
                    ?: super.onReceivedIcon(view, icon)
            }

            override fun onReceivedTouchIconUrl(
                view: WebView?,
                url: String?,
                precomposed: Boolean
            ) {
                customWebChromeClient?.onReceivedTouchIconUrl(view, url, precomposed)
                    ?: super.onReceivedTouchIconUrl(view, url, precomposed)
            }

            override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                customWebChromeClient?.onShowCustomView(view, callback)
                    ?: super.onShowCustomView(view, callback)
            }

            override fun onShowCustomView(
                view: View?,
                requestedOrientation: Int,
                callback: CustomViewCallback?
            ) {
                customWebChromeClient?.onShowCustomView(view, requestedOrientation, callback)
                    ?: super.onShowCustomView(view, requestedOrientation, callback)
            }

            override fun onHideCustomView() {
                customWebChromeClient?.onHideCustomView()
                    ?: super.onHideCustomView()
            }

            override fun onCreateWindow(
                view: WebView?,
                isDialog: Boolean,
                isUserGesture: Boolean,
                resultMsg: Message?
            ): Boolean {
                return customWebChromeClient?.onCreateWindow(
                    view,
                    isDialog,
                    isUserGesture,
                    resultMsg
                )
                    ?: super.onCreateWindow(view, isDialog, isUserGesture, resultMsg)
            }

            override fun onRequestFocus(view: WebView?) {
                customWebChromeClient?.onRequestFocus(view)
                    ?: super.onRequestFocus(view)
            }

            override fun onCloseWindow(window: WebView?) {
                customWebChromeClient?.onCloseWindow(window)
                    ?: super.onCloseWindow(window)
            }

            override fun onJsAlert(
                view: WebView?,
                url: String?,
                message: String?,
                result: JsResult?
            ): Boolean {
                return customWebChromeClient?.onJsAlert(view, url, message, result)
                    ?: super.onJsAlert(view, url, message, result)
            }

            override fun onJsConfirm(
                view: WebView?,
                url: String?,
                message: String?,
                result: JsResult?
            ): Boolean {
                return customWebChromeClient?.onJsConfirm(view, url, message, result)
                    ?: super.onJsConfirm(view, url, message, result)
            }

            override fun onJsPrompt(
                view: WebView?,
                url: String?,
                message: String?,
                defaultValue: String?,
                result: JsPromptResult?
            ): Boolean {
                return customWebChromeClient?.onJsPrompt(view, url, message, defaultValue, result)
                    ?: super.onJsPrompt(view, url, message, defaultValue, result)
            }

            override fun onJsBeforeUnload(
                view: WebView?,
                url: String?,
                message: String?,
                result: JsResult?
            ): Boolean {
                return customWebChromeClient?.onJsBeforeUnload(view, url, message, result)
                    ?: super.onJsBeforeUnload(view, url, message, result)
            }

            override fun onGeolocationPermissionsShowPrompt(origin: String?, callback: Callback?) {
                if (enabledGeolocation) {
                    callback?.invoke(origin, true, false)
                } else {
                    customWebChromeClient?.onGeolocationPermissionsShowPrompt(
                        origin,
                        callback
                    ) ?: super.onGeolocationPermissionsShowPrompt(origin, callback)
                }
            }

            override fun onGeolocationPermissionsHidePrompt() {
                customWebChromeClient?.onGeolocationPermissionsHidePrompt()
                    ?: super.onGeolocationPermissionsHidePrompt()
            }

            @SuppressLint("NewApi")
            override fun onPermissionRequest(request: PermissionRequest?) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    customWebChromeClient?.onPermissionRequest(request)
                        ?: super.onPermissionRequest(request)
                }
            }

            @SuppressLint("NewApi")
            override fun onPermissionRequestCanceled(request: PermissionRequest?) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    customWebChromeClient?.onPermissionRequestCanceled(request)
                        ?: super.onPermissionRequestCanceled(request)
                }
            }

            override fun onJsTimeout(): Boolean {
                return customWebChromeClient?.onJsTimeout() ?: super.onJsTimeout()
            }

            override fun onConsoleMessage(message: String?, lineNumber: Int, sourceID: String?) {
                customWebChromeClient?.onConsoleMessage(message, lineNumber, sourceID)
                    ?: super.onConsoleMessage(message, lineNumber, sourceID)
            }

            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                return customWebChromeClient?.onConsoleMessage(consoleMessage)
                    ?: super.onConsoleMessage(consoleMessage)
            }

            override fun getDefaultVideoPoster(): Bitmap? {
                return customWebChromeClient?.defaultVideoPoster ?: super.getDefaultVideoPoster()
            }

            override fun getVideoLoadingProgressView(): View? {
                return customWebChromeClient?.videoLoadingProgressView
                    ?: super.getVideoLoadingProgressView()
            }

            override fun getVisitedHistory(callback: ValueCallback<Array<String>>?) {
                customWebChromeClient?.getVisitedHistory(callback)
                    ?: super.getVisitedHistory(callback)
            }

            override fun onExceededDatabaseQuota(
                url: String?,
                databaseIdentifier: String?,
                quota: Long,
                estimatedDatabaseSize: Long,
                totalQuota: Long,
                quotaUpdater: QuotaUpdater?
            ) {
                customWebChromeClient?.onExceededDatabaseQuota(
                    url,
                    databaseIdentifier,
                    quota,
                    estimatedDatabaseSize,
                    totalQuota,
                    quotaUpdater
                ) ?: super.onExceededDatabaseQuota(
                    url,
                    databaseIdentifier,
                    quota,
                    estimatedDatabaseSize,
                    totalQuota,
                    quotaUpdater
                )
            }

            override fun onReachedMaxAppCacheSize(
                requiredStorage: Long,
                quota: Long,
                quotaUpdater: QuotaUpdater?
            ) {
                customWebChromeClient?.onReachedMaxAppCacheSize(
                    requiredStorage,
                    quota,
                    quotaUpdater
                ) ?: super.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater)
            }
        })

        setDownloadListener { url, userAgent, contentDisposition, mimeType, contentLength ->
            val suggestedFilename = URLUtil.guessFileName(url, contentDisposition, mimeType)
            listener?.onDownloadRequested(
                url,
                suggestedFilename,
                mimeType,
                contentLength,
                contentDisposition,
                userAgent
            )
        }
    }

    override fun loadUrl(url: String, additionalHttpHeaders: MutableMap<String, String>) {
        additionalHttpHeaders === additionalHttpHeaders?.putAll(httpHeaders) ?: httpHeaders
        super.loadUrl(url, additionalHttpHeaders)
    }

    override fun loadUrl(url: String) {
        if (httpHeaders.isNotEmpty()) {
            super.loadUrl(url, httpHeaders)
        } else {
            super.loadUrl(url)
        }
    }

    fun loadUrl(url: String, preventCaching: Boolean) {
        var url = url
        if (preventCaching) {
            url = makeUrlUnique(url)
        }

        loadUrl(url)
    }

    fun loadUrl(
        url: String,
        preventCaching: Boolean,
        additionalHttpHeaders: MutableMap<String, String>
    ) {
        var url = url
        if (preventCaching) {
            url = makeUrlUnique(url)
        }
        loadUrl(url, additionalHttpHeaders)
    }

    fun isPermittedUrl(url: String?): Boolean {
        // if the permitted hostnames have not been restricted to a specific set
        if (lstPermittedHostNames.size == 0) {
            // all hostnames are allowed
            return true
        }

        val parsedUrl = Uri.parse(url)

        // get the hostname of the URL that is to be checked
        val actualHost = parsedUrl.host ?: return false

        // if the hostname could not be determined, usually because the URL has been invalid

        // if the host contains invalid characters (e.g. a backslash)
        if (!actualHost.matches("^[a-zA-Z0-9._!~*')(;:&=+$,%\\[\\]-]*$".toRegex())) {
            // prevent mismatches between interpretations by `Uri` and `WebView`, e.g. for `http://evil.example.com\.good.example.com/`
            return false
        }

        // get the user information from the authority part of the URL that is to be checked
        val actualUserInformation = parsedUrl.userInfo

        // if the user information contains invalid characters (e.g. a backslash)
        actualUserInformation?.let {
            // prevent mismatches between interpretations by `Uri` and `WebView`, e.g. for `http://evil.example.com\@good.example.com/`
            if (it.matches("^[a-zA-Z0-9._!~*')(;:&=+$,%-]*$".toRegex())) return false
        }

        // for every hostname in the set of permitted hosts
        for (expectedHost in lstPermittedHostNames) {
            // if the two hostnames match or if the actual host is a subdomain of the expected host
            if (actualHost == expectedHost || actualHost.endsWith(".$expectedHost")) {
                // the actual hostname of the URL to be checked is allowed
                return true
            }
        }

        // the actual hostname of the URL to be checked is not allowed since there were no matches
        return false
    }

    @Deprecated("use isPermittedUrl method instead", ReplaceWith("isPermittedUrl(url)"))
    private fun isHostnameAllowed(url: String): Boolean {
        return isPermittedUrl(url)
    }

    private fun setLastError() {
        lastError = System.currentTimeMillis()
    }

    private fun hasError(): Boolean {
        return lastError + 500 >= System.currentTimeMillis()
    }

    @SuppressLint("NewApi")
    private fun openFileInput(
        fileUploadCallbackFirst: ValueCallback<Uri>?,
        fileUploadCallbackSecond: ValueCallback<Array<Uri>>?,
        allowMultiple: Boolean
    ) {
        this.fileUploadCallbackFirst?.onReceiveValue(null)
        this.fileUploadCallbackFirst = fileUploadCallbackFirst
        this.fileUploadCallbackSecond?.onReceiveValue(null)
        this.fileUploadCallbackSecond = fileUploadCallbackSecond

        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.addCategory(Intent.CATEGORY_OPENABLE)

        if (allowMultiple && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }

        i.type = uploadFileTypes

        fragment?.get()?.startActivityForResult(
            Intent.createChooser(i, fileUploadPromptLabel),
            requestCodeFilePicker
        )
            ?: activity?.get()?.startActivityForResult(
                Intent.createChooser(i, fileUploadPromptLabel),
                requestCodeFilePicker
            )
    }

    /** Wrapper for methods related to alternative browsers that have their own rendering engines  */
    object Browsers {

        /** Package name of an alternative browser that is installed on this device  */
        private var mAlternativePackage: String? = null

        /**
         * Returns whether there is an alternative browser with its own rendering engine currently installed
         *
         * @param context a valid `Context` reference
         * @return whether there is an alternative browser or not
         */
        fun hasAlternative(context: Context): Boolean {
            return getAlternative(context) != null
        }

        /**
         * Returns the package name of an alternative browser with its own rendering engine or `null`
         *
         * @param context a valid `Context` reference
         * @return the package name or `null`
         */
        @JvmStatic
        fun getAlternative(context: Context): String? {
            if (mAlternativePackage != null) {
                return mAlternativePackage
            }
            val apps =
                context.packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            for (app in apps) {
                if (!app.enabled) {
                    continue
                }
                if (alternativeBrowsers.contains(app.packageName)) {
                    mAlternativePackage = app.packageName
                    return app.packageName
                }
            }

            return null
        }

        /**
         * Opens the given URL in an alternative browser
         *
         * @param context a valid `Activity` reference
         * @param url the URL to open
         */
        @JvmStatic
        fun openUrl(context: Activity, url: String) {
            openUrl(context, url, false)
        }

        /**
         * Opens the given URL in an alternative browser
         *
         * @param context a valid `Activity` reference
         * @param url the URL to open
         * @param withoutTransition whether to switch to the browser `Activity` without a transition
         */
        @JvmStatic
        fun openUrl(context: Activity, url: String, withoutTransition: Boolean = false) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.setPackage(getAlternative(context))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            context.startActivity(intent)

            if (withoutTransition) {
                context.overridePendingTransition(0, 0)
            }
        }

    }

    companion object {
        const val PACKAGE_NAME_DOWNLOAD_MANAGER = "com.android.providers.downloads"
        const val REQUEST_CODE_FILE_PICKER = 51426
        const val DATABASES_SUB_FOLDER = "/databases"
        const val LANGUAGE_DEFAULT_ISO3 = "eng"

        /** Alternative browsers that have their own rendering engine and *may* be installed on this device  */
        val alternativeBrowsers = arrayOf(
            "org.mozilla.firefox",
            "com.android.chrome",
            "com.opera.browser",
            "org.mozilla.firefox_beta",
            "com.chrome.beta",
            "com.opera.browser.beta"
        )

        @SuppressLint("NewApi")
        fun setAllowAccessFromFileUrls(webSettings: WebSettings, allowed: Boolean) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                webSettings.allowFileAccessFromFileURLs = allowed
                webSettings.allowUniversalAccessFromFileURLs = allowed
            }
        }

        fun makeUrlUnique(url: String): String {
            val unique = StringBuilder()
            unique.append(url)

            if (url.contains("?")) {
                unique.append('&')
            } else {
                if (url.lastIndexOf('/') <= 7) {
                    unique.append('/')
                }
                unique.append('?')
            }

            unique.append(System.currentTimeMillis())
            unique.append('=')
            unique.append(1)

            return unique.toString()
        }

        val languageIso3: String
            get() {
                return try {
                    Locale.getDefault().isO3Language.toLowerCase(Locale.US)
                } catch (e: MissingResourceException) {
                    LANGUAGE_DEFAULT_ISO3
                }

            }

        @Throws(IllegalArgumentException::class, UnsupportedEncodingException::class)
        fun decodeBase64(base64: String): String {
            val bytes = Base64.decode(base64, Base64.DEFAULT)
            return String(bytes)
        }

        /**
         * Returns whether file uploads can be used on the current device (generally all platform versions except for 4.4)
         *
         * @return whether file uploads can be used
         */
        val isFileUploadAvailable: Boolean
            get() = isFileUploadAvailable(false)

        /**
         * Returns whether file uploads can be used on the current device (generally all platform versions except for 4.4)
         *
         * On Android 4.4.3/4.4.4, file uploads may be possible but will come with a wrong MIME type
         *
         * @param needsCorrectMimeType whether a correct MIME type is required for file uploads or `application/octet-stream` is acceptable
         * @return whether file uploads can be used
         */
        @JvmStatic
        fun isFileUploadAvailable(needsCorrectMimeType: Boolean): Boolean {
            return if (Build.VERSION.SDK_INT === Build.VERSION_CODES.KITKAT) {
                val platformVersion =
                    if (Build.VERSION.RELEASE == null) "" else Build.VERSION.RELEASE

                !needsCorrectMimeType && (platformVersion.startsWith("4.4.3") || platformVersion.startsWith(
                    "4.4.4"
                ))
            } else {
                true
            }
        }

        /**
         * Handles a download by loading the file from `fromUrl` and saving it to `toFilename` on the external storage
         *
         * This requires the two permissions `android.permission.INTERNET` and `android.permission.WRITE_EXTERNAL_STORAGE`
         *
         * Only supported on API level 9 (Android 2.3) and above
         *
         * @param context a valid `Context` reference
         * @param fromUrl the URL of the file to download, e.g. the one from `AdvancedWebView.onDownloadRequested(...)`
         * @param toFilename the name of the destination file where the download should be saved, e.g. `myImage.jpg`
         * @return whether the download has been successfully handled or not
         * @throws IllegalStateException if the storage or the target directory could not be found or accessed
         */
        @SuppressLint("NewApi")
        fun handleDownload(context: Context, fromUrl: String, toFilename: String): Boolean {
            val request = Request(Uri.parse(fromUrl))
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, toFilename)

            val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            return try {
                dm.enqueue(request)
                true
            } catch (e: IllegalArgumentException) {
                // show the settings screen where the user can enable the download manager app again
                openAppSettings(context, PACKAGE_NAME_DOWNLOAD_MANAGER)
                false
            }
            // if the download manager app has been disabled on the device
        }

        @SuppressLint("NewApi")
        private fun openAppSettings(context: Context, packageName: String): Boolean {
            return try {
                val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
                true
            } catch (e: Exception) {
                false
            }

        }
    }

    private fun addJavascriptInterface() {
        if (!addedJavascriptInterface) {
            // This JsInterface must match Javascript interface name of VideoEnabledWebChromeClient
            super.addJavascriptInterface(
                JavascriptInterface(),
                Constants.WEB_VIEW_JAVASCRIPT_INTERFACE_NAME
            )
            addedJavascriptInterface = true
        }
    }

    inner class JavascriptInterface {
        @android.webkit.JavascriptInterface
        fun notifyVideoEnd() {
            // Must match Javascript interface method of VideoEnabledWebChromeClient
            Timber.d("notifyVideoEnd: GOT IT !!!")
            // This code is not executed in the UI thread, so we must force that to happen
            Handler(Looper.getMainLooper()).post {
                customWebChromeClient?.onHideCustomView()
            }
        }
    }

}
