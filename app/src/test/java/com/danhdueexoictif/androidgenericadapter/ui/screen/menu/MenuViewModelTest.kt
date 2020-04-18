package com.danhdueexoictif.androidgenericadapter.ui.screen.menu

import com.danhdueexoictif.androidgenericadapter.BaseViewModelTest
import io.mockk.spyk
import junit.framework.Assert.assertNotNull
import org.junit.Test

class MenuViewModelTest : BaseViewModelTest<MenuViewModel>() {

    override fun setup() {
        super.setup()
        viewModel = spyk(MenuViewModel())
    }

    @Test
    fun testSetup() {
        assertNotNull(viewModel)
    }
}