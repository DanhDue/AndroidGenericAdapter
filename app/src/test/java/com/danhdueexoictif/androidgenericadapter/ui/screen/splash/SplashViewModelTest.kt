package com.danhdueexoictif.androidgenericadapter.ui.screen.splash

import com.danhdueexoictif.androidgenericadapter.BaseViewModelTest
import io.mockk.spyk
import org.junit.Assert.assertNotNull
import org.junit.Test

class SplashViewModelTest : BaseViewModelTest<SplashViewModel>() {

    override fun setup() {
        super.setup()
        viewModel = spyk(SplashViewModel())
    }

    @Test
    fun testSetup() {
        assertNotNull(viewModel)
    }
}