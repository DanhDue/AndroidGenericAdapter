package com.danhdueexoictif.androidgenericadapter.data.remote

import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.matchers.types.shouldNotBeSameInstanceAs
import io.kotlintest.shouldBe
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.*
import java.io.IOException

internal class NetworkResponseCallTest {
    private val errorConverter = Converter<ResponseBody, String> { it.string() }
    private val backingCall =
        CompletableCall<String>()
    private val networkResponseCall =
        NetworkResponseCall<String, String>(backingCall, errorConverter)

    @Test(expected = UnsupportedOperationException::class)
    fun `should throw an error when invoking 'execute'`() {
        networkResponseCall.execute()
    }

    @Test
    fun `should delegate properties to backing call`() {
        with(networkResponseCall) {
            isExecuted shouldBe backingCall.isExecuted
            isCanceled shouldBe backingCall.isCanceled
            request() shouldBe backingCall.request()
        }
    }

    @Test
    fun `should return new instance when cloned`() {
        val clonedCall = networkResponseCall.clone()
        clonedCall shouldNotBeSameInstanceAs networkResponseCall
    }

    @Test
    fun `should cancel backing call as well when canceled`() {
        networkResponseCall.cancel()
        assert(backingCall.isCanceled)
    }

    @Test
    fun `should parse successful call as NetworkResponse Success`() {
        val body = "Test body"
        networkResponseCall.enqueue(object : Callback<NetworkResponse<String, String>> {
            override fun onResponse(
                call: Call<NetworkResponse<String, String>>,
                response: Response<NetworkResponse<String, String>>
            ) {
                assert(response.isSuccessful)
                response.body().shouldBeTypeOf<NetworkResponse.Success<String>>()
                (response.body()!! as NetworkResponse.Success).body shouldBe body
            }

            override fun onFailure(call: Call<NetworkResponse<String, String>>, t: Throwable) {
                throw IllegalStateException()
            }
        })
        backingCall.complete(body)
    }

    @Test
    fun `should parse unsuccessful call with HttpException as NetworkResponse ServerError`() {
        networkResponseCall.enqueue(object : Callback<NetworkResponse<String, String>> {
            override fun onResponse(
                call: Call<NetworkResponse<String, String>>,
                response: Response<NetworkResponse<String, String>>
            ) {
                response.body().shouldBeTypeOf<NetworkResponse.ServerError<String>>()
            }

            override fun onFailure(call: Call<NetworkResponse<String, String>>, t: Throwable) {
                throw IllegalStateException()
            }
        })

        backingCall.completeWithException(
            HttpException(
                Response.error<String>(
                    404,
                    "Server Error".toResponseBody()
                )
            )
        )
    }

    @Test
    fun `should parse error body correctly when ServerError occurs`() {

        val errorBody = "An error occurred"
        val responseCode = 404

        networkResponseCall.enqueue(object : Callback<NetworkResponse<String, String>> {
            override fun onResponse(
                call: Call<NetworkResponse<String, String>>,
                response: Response<NetworkResponse<String, String>>
            ) {
                with(response.body()) {
                    this as NetworkResponse.ServerError<String>
                    body shouldBe errorBody
                    code shouldBe responseCode
                }
            }

            override fun onFailure(call: Call<NetworkResponse<String, String>>, t: Throwable) {
                throw IllegalStateException()
            }
        })
        backingCall.complete(Response.error(responseCode, errorBody.toResponseBody()))
    }

    @Test
    fun `should parse unsuccessful call with IOException as NetworkResponse NetworkError`() {
        networkResponseCall.enqueue(object : Callback<NetworkResponse<String, String>> {
            override fun onFailure(call: Call<NetworkResponse<String, String>>, t: Throwable) {
                throw IllegalStateException()
            }

            override fun onResponse(
                call: Call<NetworkResponse<String, String>>,
                response: Response<NetworkResponse<String, String>>
            ) {
                response.body().shouldBeTypeOf<NetworkResponse.NetworkError>()
            }
        })

        backingCall.completeWithException(IOException())
    }

    @Test
    fun `should parse successful call with empty body as NetworkResponse ServerError`() {
        networkResponseCall.enqueue(object : Callback<NetworkResponse<String, String>> {
            override fun onFailure(call: Call<NetworkResponse<String, String>>, t: Throwable) {
                throw IllegalStateException()
            }

            override fun onResponse(
                call: Call<NetworkResponse<String, String>>,
                response: Response<NetworkResponse<String, String>>
            ) {
                response.body().shouldBeTypeOf<NetworkResponse.ServerError<String>>()
            }
        })

        backingCall.complete(Response.error(404, "".toResponseBody()))
    }
}
