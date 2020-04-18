package com.danhdueexoictif.androidgenericadapter.ui.screen.login

import com.danhdueexoictif.androidgenericadapter.BaseViewModelTest
import io.mockk.spyk
import org.junit.Assert.assertNotNull
import org.junit.Test

class LoginViewModelTest : BaseViewModelTest<LoginViewModel>() {
    override fun setup() {
        super.setup()
        viewModel = spyk(LoginViewModel())
    }

    @Test
    fun testSetup() {
        assertNotNull(viewModel)
    }
}