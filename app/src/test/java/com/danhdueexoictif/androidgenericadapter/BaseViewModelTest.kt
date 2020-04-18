package com.danhdueexoictif.androidgenericadapter

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.danhdueexoictif.androidgenericadapter.di.appModule
import com.danhdueexoictif.androidgenericadapter.ui.base.BaseViewModel
import io.mockk.MockKAnnotations
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.stubbing.OngoingStubbing

abstract class BaseViewModelTest<T : BaseViewModel> : KoinTest {

    protected lateinit var viewModel: T
    private var context = mockk<Context>()

    @Rule
    @JvmField
    val instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @Before
    open fun setup() {
        MockitoAnnotations.initMocks(this)
        MockKAnnotations.init(this, relaxUnitFun = true)

        startKoin {
            androidContext(context)
            modules(appModule)
        }
    }

    @After
    open fun tearDown() {
        stopKoin()
    }

    /**
     *  Call this fun for Object class to avoid non-null error in kotlin
     */
    fun <T> any(type: Class<T>): T {
        Mockito.any(type)
        return uninitialized()
    }

    fun <T> uninitialized(): T = null as T

    fun <T> whenever(methodCall: T): OngoingStubbing<T> = Mockito.`when`(methodCall)
}
