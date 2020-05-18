package com.danhdueexoictif.androidgenericadapter.data.remote

import com.danhdueexoictif.androidgenericadapter.BuildConfig
import okhttp3.Headers
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import java.io.EOFException
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets.UTF_8

class ChangeableBaseUrlInterceptor : Interceptor {
    @Volatile
    private var host: HttpUrl? = null

    @Volatile
    private var headersToRedact = emptySet<String>()

    /**
     * TODO: Call this one to change to another domain url.
     * @param apiDomain: the new api domain url.
     */
    fun setApiDomain(apiDomain: String) {
        this.host = apiDomain.toHttpUrlOrNull()
    }

    /**
     * TODO: Call this one to return the origin base url.
     */
    fun clearApiDomain() {
        host = null
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()
        host?.let {
            val newHost = HttpUrl.Builder()
                .scheme(it.scheme)
                .host(it.toUrl().toURI().host)
                .port(it.port)
            for (pathSegment in it.encodedPathSegments) {
                if (pathSegment.isNotBlank()) newHost.addPathSegment(pathSegment)
            }
            newHost.addPathSegment(request.url.encodedPathSegments.last())
            val newUrl = newHost.build()
            val oldUrl = request.url
            request = request.newBuilder().url(newUrl).build()
            HttpLoggingInterceptor.Logger.DEFAULT.log("--> Change request from $oldUrl to $newUrl")
            logRequest(chain)
        }
        return chain.proceed(request)
    }

    /**
     * log request's information that is changed host follow configuration result.
     * @param chain that is overridden.
     */
    private fun logRequest(chain: Interceptor.Chain) {
        val request = chain.request()

        val level: HttpLoggingInterceptor.Level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

        val logBody = level == HttpLoggingInterceptor.Level.BODY
        val logHeaders = logBody || level == HttpLoggingInterceptor.Level.HEADERS

        val requestBody = request.body

        val connection = chain.connection()
        var requestStartMessage =
            ("--> ${request.method} ${request.url}${if (connection != null) " " + connection.protocol() else ""}")
        if (!logHeaders && requestBody != null) {
            requestStartMessage += " (${requestBody.contentLength()}-byte body)"
        }
        HttpLoggingInterceptor.Logger.DEFAULT.log(requestStartMessage)

        if (logHeaders) {
            val headers = request.headers

            if (requestBody != null) {
                // Request body headers are only present when installed as a network interceptor. When not
                // already present, force them to be included (if available) so their values are known.
                requestBody.contentType()?.let {
                    if (headers["Content-Type"] == null) {
                        HttpLoggingInterceptor.Logger.DEFAULT.log("Content-Type: $it")
                    }
                }
                if (requestBody.contentLength() != -1L) {
                    if (headers["Content-Length"] == null) {
                        HttpLoggingInterceptor.Logger.DEFAULT.log("Content-Length: ${requestBody.contentLength()}")
                    }
                }
            }

            for (i in 0 until headers.size) {
                logHeader(headers, i)
            }

            if (!logBody || requestBody == null) {
                HttpLoggingInterceptor.Logger.DEFAULT.log("--> END ${request.method}")
            } else if (bodyHasUnknownEncoding(request.headers)) {
                HttpLoggingInterceptor.Logger.DEFAULT.log("--> END ${request.method} (encoded body omitted)")
            } else if (requestBody.isDuplex()) {
                HttpLoggingInterceptor.Logger.DEFAULT.log("--> END ${request.method} (duplex request body omitted)")
            } else if (requestBody.isOneShot()) {
                HttpLoggingInterceptor.Logger.DEFAULT.log("--> END ${request.method} (one-shot body omitted)")
            } else {
                val buffer = Buffer()
                requestBody.writeTo(buffer)

                val contentType = requestBody.contentType()
                val charset: Charset = contentType?.charset(UTF_8) ?: UTF_8

                HttpLoggingInterceptor.Logger.DEFAULT.log("")
                if (buffer.isProbablyUtf8()) {
                    HttpLoggingInterceptor.Logger.DEFAULT.log(buffer.readString(charset))
                    HttpLoggingInterceptor.Logger.DEFAULT.log("--> END ${request.method} (${requestBody.contentLength()}-byte body)")
                } else {
                    HttpLoggingInterceptor.Logger.DEFAULT.log(
                        "--> END ${request.method} (binary ${requestBody.contentLength()}-byte body omitted)"
                    )
                }
            }
        }
    }

    /**
     * log request header information.
     */
    private fun logHeader(headers: Headers, i: Int) {
        val value = if (headers.name(i) in headersToRedact) "██" else headers.value(i)
        HttpLoggingInterceptor.Logger.DEFAULT.log(headers.name(i) + ": " + value)
    }

    /**
     * detect encoding has set on request body or not
     */
    private fun bodyHasUnknownEncoding(headers: Headers): Boolean {
        val contentEncoding = headers["Content-Encoding"] ?: return false
        return !contentEncoding.equals("identity", ignoreCase = true) &&
                !contentEncoding.equals("gzip", ignoreCase = true)
    }
}

/**
 * Returns true if the body in question probably contains human readable text. Uses a small
 * sample of code points to detect unicode control characters commonly used in binary file
 * signatures.
 */
private fun Buffer.isProbablyUtf8(): Boolean {
    try {
        val prefix = Buffer()
        val byteCount = size.coerceAtMost(64)
        copyTo(prefix, 0, byteCount)
        for (i in 0 until 16) {
            if (prefix.exhausted()) {
                break
            }
            val codePoint = prefix.readUtf8CodePoint()
            if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                return false
            }
        }
        return true
    } catch (_: EOFException) {
        return false // Truncated UTF-8 sequence.
    }
}
