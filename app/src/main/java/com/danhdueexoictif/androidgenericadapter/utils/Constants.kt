package com.danhdueexoictif.androidgenericadapter.utils

object Constants {
    // Crashlytics
    const val CRASHLYTICS_KEY_PRIORITY = "priority"
    const val CRASHLYTICS_KEY_TAG = "tag"
    const val CRASHLYTICS_KEY_MESSAGE = "message"

    // Load more
    const val DEFAULT_FIRST_PAGE = 1
    const val DEFAULT_NUM_VISIBLE_THRESHOLD = 6
    const val DEFAULT_ITEM_PER_PAGE = 5

    const val THRESHOLD_CLICK_TIME = 1000
    const val THRESHOLD_MOVE_TIME = 1500
    const val THRESHOLD_MOVE_SELECTION_STATE_CHANGE_TIME = 300
    const val LOADING_DIALOG_DISMISS_DELAY_TIME = 3000L
    const val CLOSE_APP_DELAY_TIME = 2000L

    const val REGEX_EXCLUDE_HIRAGANA = """;[^}]+\}"""

    const val WEB_VIEW_JAVASCRIPT_INTERFACE_NAME = "_AdvancedWebView"

    const val VIDEO_CACHE_PATH = "/d3t3_cache"

    // SharedPreference
    const val DEF_ACCESS_TOKEN = ""
    const val DEF_REFRESH_TOKEN = ""
    const val DEF_EXPIRE_IN = 0L

    object NetworkRequestParam {
        const val OS_TYPE = "os-type"
        const val CONTENT_TYPE = "Content-Type"
        const val APP_VERSION = "version"
        const val AUTHORIZATION = "Authorization"
    }

    object NetworkRequestValue {
        const val OS_NAME = "Android"
        const val CONTENT_TYPE = "application/json"
    }
}
