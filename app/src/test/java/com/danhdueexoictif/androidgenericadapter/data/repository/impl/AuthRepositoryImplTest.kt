package com.danhdueexoictif.androidgenericadapter.data.repository.impl

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.danhdueexoictif.androidgenericadapter.BuildConfig
import com.danhdueexoictif.androidgenericadapter.MainCoroutineRule
import com.danhdueexoictif.androidgenericadapter.data.bean.LogoutResponse
import com.danhdueexoictif.androidgenericadapter.data.bean.MemberResObject
import com.danhdueexoictif.androidgenericadapter.data.bean.OauthToken
import com.danhdueexoictif.androidgenericadapter.data.local.pref.SharedPreferenceHelper
import com.danhdueexoictif.androidgenericadapter.data.remote.ApiService
import com.danhdueexoictif.androidgenericadapter.data.remote.NetworkResponse
import com.danhdueexoictif.androidgenericadapter.data.remote.invoke
import com.danhdueexoictif.androidgenericadapter.data.remote.request.MemberReqObject
import com.danhdueexoictif.androidgenericadapter.data.remote.response.HttpResponseCode
import com.danhdueexoictif.androidgenericadapter.data.repository.AuthRepository
import com.danhdueexoictif.androidgenericadapter.runBlocking
import com.danhdueexoictif.androidgenericadapter.utils.extension.getAccessToken
import io.kotlintest.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class AuthRepositoryImplTest {
    @Rule
    @JvmField
    val instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private lateinit var authRepo: AuthRepository

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
        authRepo = spyk(AuthRepositoryImpl(apiService, sharedPreferenceHelper))
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
        val result = authRepo.createBrandMemberIdAsync("d3t3").await()
        // Then
        result() shouldBe expectedResult
    }

    @Test
    fun logout_ifSuccess() = coroutinesRule.runBlocking {
        // Given
        every {
            runBlocking {
                apiService.logout(
                    "123".getAccessToken() ?: "",
                    "123"
                )
            }
        } returns NetworkResponse.Success(
            LogoutResponse("logout successfully!")
        )
        // When
        val response = authRepo.logout()
        // Then
        Assert.assertEquals("logout successfully!", response()?.message)
    }

    @Test
    fun logout_ifFail() = coroutinesRule.runBlocking {
        // Given
        every { sharedPreferenceHelper.getAccessToken() } returns ""
        // When
        val response = authRepo.logout()
        // Then
        assertEquals(
            (response as NetworkResponse.ServerError).code,
            HttpResponseCode.HTTP_NOT_FOUND
        )
    }

    @Test
    fun loginWithGoogleAsync_ifSuccess() = coroutinesRule.runBlocking {
        // Given
        val expectedResponse = NetworkResponse.Success(OauthToken(""))
        every {
            runBlocking {
                apiService.login("", 1, "", AuthRepositoryImpl.GOOGLE, "accessToken").await()
            }
        } returns expectedResponse
        // When
        val result = authRepo.loginWithGoogleAsync("accessToken").await()
        // Then
        result shouldBe expectedResponse
    }
}