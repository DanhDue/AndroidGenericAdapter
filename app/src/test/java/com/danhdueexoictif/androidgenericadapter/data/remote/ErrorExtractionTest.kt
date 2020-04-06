package com.danhdueexoictif.androidgenericadapter.data.remote

import io.kotlintest.matchers.types.shouldBeSameInstanceAs
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import okhttp3.Headers
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.Converter
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

internal class ErrorExtractionTest {

    private val errorConverter =
        Converter<ResponseBody, String> { responseBody -> responseBody.toString() }

    @Test
    fun `extract error from HttpException test`() {
        val serverResponse = "Server Error".toResponseBody()
        val response = Response.error<String>(404, serverResponse)
        val exception = HttpException(response)
        with(exception.extractFromHttpException<String, String>(errorConverter)) {

            shouldBeTypeOf<NetworkResponse.ServerError<String>>()
            this as NetworkResponse.ServerError

            body shouldBe errorConverter.convert(serverResponse)
            code shouldBe 404
            headers shouldBe Headers.headersOf()
        }
    }

    @Test
    fun `extract error from IOException test`() {
        val ioException = IOException()
        with(ioException.extractNetworkResponse<String, String>(errorConverter)) {

            shouldBeTypeOf<NetworkResponse.NetworkError>()
            this as NetworkResponse.NetworkError

            error shouldBeSameInstanceAs ioException
        }
    }

    private class CustomException : Exception()

    @Test(expected = CustomException::class)
    fun `extract error from an unknown exception test`() {
        val exception =
            CustomException()
        exception.extractNetworkResponse<String, String>(errorConverter)
    }
}
