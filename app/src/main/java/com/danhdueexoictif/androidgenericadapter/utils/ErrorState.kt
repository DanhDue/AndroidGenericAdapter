package com.danhdueexoictif.androidgenericadapter.utils

import androidx.annotation.IntDef

class ErrorState(
    @ErrorDef var type: Int? = -1,
    var title: String?,
    var message: String?
) {
    companion object {
        const val NO_INTERNET_CONNECTION = 0
        const val SERVER_MAINTENANCE = 1
        const val COMMON_ERROR = 2

        @IntDef(
            NO_INTERNET_CONNECTION,
            SERVER_MAINTENANCE,
            COMMON_ERROR
        )
        @Retention(AnnotationRetention.SOURCE)
        annotation class ErrorDef
    }
}
