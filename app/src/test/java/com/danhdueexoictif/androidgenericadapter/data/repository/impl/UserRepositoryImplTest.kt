package com.danhdueexoictif.androidgenericadapter.data.repository.impl

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.danhdueexoictif.androidgenericadapter.data.local.pref.SharedPreferenceHelper
import com.danhdueexoictif.androidgenericadapter.data.repository.UserRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.verify
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class UserRepositoryImplTest {
    @Rule
    @JvmField
    val instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private lateinit var userRepo: UserRepository

    @MockK
    private lateinit var sharedPreferenceHelper: SharedPreferenceHelper

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        userRepo = spyk(UserRepositoryImpl(sharedPreferenceHelper))
    }

    @Test
    fun getUUID_isCall() {
        // Given
        val expectedUUID = "UUID"
        every { sharedPreferenceHelper.getUUID() } returns expectedUUID
        // When
        val result = userRepo.getUUID()
        // Then
        assertEquals(result, expectedUUID)
    }

    @Test
    fun saveAppStartFirstTime_isCall() {
        // Given
        // When
        userRepo.saveAppStartFirstTime(true)
        // Then
        verify { sharedPreferenceHelper.setStartAppFirstTime(true) }
    }

    @Test
    fun isAppStartFirstTime_isCall() {
        // Given
        every { sharedPreferenceHelper.isStartAppFirstTime() } returns true
        // When
        val result = userRepo.isAppStartFirstTime()
        // Then
        verify { sharedPreferenceHelper.isStartAppFirstTime() }
        assertEquals(result, true)
    }
}