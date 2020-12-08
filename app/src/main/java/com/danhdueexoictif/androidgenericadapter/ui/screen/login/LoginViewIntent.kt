package com.danhdueexoictif.androidgenericadapter.ui.screen.login

import com.danhdueexoictif.androidgenericadapter.ui.base.mvi.ViewIntent

sealed class LoginViewIntent : ViewIntent {
    object LoadInitViewIntent : LoginViewIntent()
    object SubmitViewIntent : LoginViewIntent()
    object GoToForgotPasswordViewIntent : LoginViewIntent()
}