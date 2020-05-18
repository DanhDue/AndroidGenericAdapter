package com.danhdueexoictif.androidgenericadapter.data.remote

import android.content.res.AssetManager
import com.danhdueexoictif.androidgenericadapter.BuildConfig
import com.danhdueexoictif.androidgenericadapter.data.remote.response.HttpResponseCode.HTTP_OK
import com.danhdueexoictif.androidgenericadapter.utils.extension.getJsonStringFromFile
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

/**
 * This will help us to test our networking code while a particular API is not implemented
 * yet on Backend side.
 */
class MockInterceptor(private val assets: AssetManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (BuildConfig.DEBUG) {
            val uri = chain.request().url.toUri().toString()
            val responseString = when {
                uri.endsWith(SAMPLE_REQUEST) -> getJsonStringFromFile(assets, MOCK_SAMPLE_RESPONSE_FILE_NAME)
                else -> ""
            }

            return if (responseString.isNotEmpty()) {
                chain.proceed(chain.request())
                    .newBuilder()
                    .code(HTTP_OK)
                    .protocol(Protocol.HTTP_2)
                    .message(responseString)
                    .body(
                        responseString.toByteArray()
                            .toResponseBody("application/json".toMediaTypeOrNull())
                    )
                    .addHeader("content-type", "application/json")
                    .build()
            } else {
                chain.proceed(chain.request())
            }
        } else {
            //just to be on safe side.
            throw IllegalAccessError(
                "MockInterceptor is only meant for Testing Purposes and " +
                        "bound to be used only with DEBUG mode"
            )
        }
    }

    companion object {
        const val SAMPLE_REQUEST = "SWFR01011991.seam"
        const val MOCK_SAMPLE_RESPONSE_FILE_NAME = "SWFR01011991.json"
    }

}
