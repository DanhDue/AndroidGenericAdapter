package com.danhdueexoictif.androidgenericadapter.data.remote

import com.danhdueexoictif.androidgenericadapter.data.remote.response.HttpResponseCode.HTTP_UNKNOWN_ERROR
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.HttpException
import java.io.IOException

/**
 * extract to NetworkResponse.NetworkError from HTTP Exception
 * @param S The success body type of [NetworkResponse]
 * @param E The error body type of [NetworkResponse]
 * @param errorConverter The Retrofit converter to convert objects from HTTP Response.
 * @return NetworkResponse.ServerError
 */
internal fun <S : Any, E : Any> HttpException.extractFromHttpException(errorConverter: Converter<ResponseBody, E>): NetworkResponse<S, E> {
    val error = response()?.errorBody()
    val responseCode = response()?.code() ?: HTTP_UNKNOWN_ERROR
    val headers = response()?.headers()
    val errorBody = when {
        error == null -> null // No error content available
        error.contentLength() == 0L -> null // Error content is empty
        else -> try {
            // There is error content present, so we should try to extract it
            errorConverter.convert(error)
        } catch (e: Exception) {
            // If unable to extract content, return with a null body and don't parse further
            return NetworkResponse.ServerError(null, responseCode, headers)
        }
    }
    return NetworkResponse.ServerError(errorBody, responseCode, headers)
}

/**
 * extract network response
 * @param S The success body type of [NetworkResponse]
 * @param E The error body type of [NetworkResponse]
 * @param errorConverter The Retrofit converter to convert objects from HTTP Response.
 * @return NetworkError if this one is an IOException like: UnknowHost, NoConnectivity,...
 * @return ServerError from HTTP response
 * @return Success from HTTP response
 */
internal fun <S : Any, E : Any> Throwable.extractNetworkResponse(errorConverter: Converter<ResponseBody, E>): NetworkResponse<S, E> {
    return when (this) {
        is IOException -> NetworkResponse.NetworkError(this)
        is HttpException -> extractFromHttpException<S, E>(errorConverter)
        else -> throw this
    }
}
