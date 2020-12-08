package com.danhdueexoictif.androidgenericadapter.ui.screen.login

import com.danhdueexoictif.androidgenericadapter.ui.base.mvi.ViewState

sealed class LoginViewState(val isLoading: Boolean = false) : ViewState {
    object Idle : LoginViewState()
    object Submitting : LoginViewState(true)

}