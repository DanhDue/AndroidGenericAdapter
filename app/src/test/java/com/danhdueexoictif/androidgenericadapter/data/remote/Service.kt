package com.danhdueexoictif.androidgenericadapter.data.remote

import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface Service {
    @GET("/")
    fun getText(): Deferred<NetworkResponse<String, String>>

    @GET("/suspend")
    suspend fun getTextSuspend(): NetworkResponse<String, String>
}
