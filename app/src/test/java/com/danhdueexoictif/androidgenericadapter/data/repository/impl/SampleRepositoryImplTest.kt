package com.danhdueexoictif.androidgenericadapter.data.repository.impl

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.danhdueexoictif.androidgenericadapter.BuildConfig
import com.danhdueexoictif.androidgenericadapter.MainCoroutineRule
import com.danhdueexoictif.androidgenericadapter.data.bean.MemberResObject
import com.danhdueexoictif.androidgenericadapter.data.bean.NewBieObject
import com.danhdueexoictif.androidgenericadapter.data.local.pref.SharedPreferenceHelper
import com.danhdueexoictif.androidgenericadapter.data.remote.ApiService
import com.danhdueexoictif.androidgenericadapter.data.remote.NetworkResponse
import com.danhdueexoictif.androidgenericadapter.data.remote.invoke
import com.danhdueexoictif.androidgenericadapter.data.remote.request.MemberReqObject
import com.danhdueexoictif.androidgenericadapter.data.remote.response.NewBieResObject
import com.danhdueexoictif.androidgenericadapter.data.repository.SampleRepository
import com.danhdueexoictif.androidgenericadapter.runBlocking
import com.danhdueexoictif.androidgenericadapter.utils.Constants
import com.danhdueexoictif.androidgenericadapter.utils.extension.getAccessToken
import io.kotlintest.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class SampleRepositoryImplTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private lateinit var sampleRepo: SampleRepository

    // Set the main coroutines dispatcher for unit testing
    @get:Rule
    var coroutinesRule = MainCoroutineRule()

    @MockK
    private lateinit var apiService: ApiService

    @MockK
    private lateinit var sharedPreferenceHelper: SharedPreferenceHelper

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        every { sharedPreferenceHelper.getAccessToken() } returns "123"
        every { sharedPreferenceHelper.getUUID() } returns "uuid"
        sampleRepo = spyk(SampleRepositoryImpl(apiService, sharedPreferenceHelper))
    }

    @Test
    fun should_doSomethingWithCoroutine() = coroutinesRule.runBlocking {
        // Given
        val expectedResult = NewBieResObject(listOf(NewBieObject(1001)))
        every { apiService.getNewbiesAsync("") } returns CompletableDeferred(
            NetworkResponse.Success(expectedResult)
        )
        // When
        val result = apiService.getNewbiesAsync("").await()
        // Then
        result() shouldBe expectedResult
    }

    @Test
    fun getNewbieList_ifSuccess() = coroutinesRule.runBlocking {
        // Given
        val expectedResult = NewBieResObject(listOf(NewBieObject(1001)))
        every { apiService.getNewbiesAsync("123".getAccessToken()) } returns CompletableDeferred(
            NetworkResponse.Success(expectedResult)
        )
        // When
        val result = sampleRepo.getNewbies(Constants.DEFAULT_FIRST_PAGE).await()
        // Then
        result() shouldBe expectedResult
    }

    @Test
    fun createBrandMemberIdAsync_ifSuccess() = coroutinesRule.runBlocking {
        // Given
        val expectedResult = MemberResObject("1000")
        every {
            apiService.createBrandMemberIdAsync(
                MemberReqObject(
                    "uuid",
                    BuildConfig.SITE_SERIAL,
                    "d3t3"
                )
            )
        } returns
                CompletableDeferred(NetworkResponse.Success(expectedResult))
        // When
        val result = sampleRepo.createBrandMemberIdAsync("d3t3").await()
        // Then
        result() shouldBe expectedResult
    }

}