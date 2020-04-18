package com.danhdueexoictif.androidgenericadapter.ui.screen.home

import com.danhdueexoictif.androidgenericadapter.BaseViewModelTest
import io.mockk.spyk
import org.junit.Assert.assertNotNull
import org.junit.Test

class HomeViewModelTest : BaseViewModelTest<HomeViewModel>() {
    override fun setup() {
        super.setup()
        viewModel = spyk(HomeViewModel())
    }

    @Test
    fun testSetup() {
        assertNotNull(viewModel)
    }
}