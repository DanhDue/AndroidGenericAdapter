package com.danhdueexoictif.androidgenericadapter.data.remote

import io.kotlintest.matchers.types.shouldBeNull
import io.kotlintest.matchers.types.shouldNotBeNull
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class ApiServiceHolderTest {

    @MockK
    private lateinit var apiService: ApiService

    private lateinit var apiServiceHolder: ApiServiceHolder

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        apiServiceHolder = spyk(ApiServiceHolder())
    }

    @Test
    fun setup_ifSuccess() {
        runBlocking { apiServiceHolder.apiService?.logout("", "") }
        apiService.shouldNotBeNull()
        apiServiceHolder.apiService = null
        apiServiceHolder.apiService.shouldBeNull()
    }
}