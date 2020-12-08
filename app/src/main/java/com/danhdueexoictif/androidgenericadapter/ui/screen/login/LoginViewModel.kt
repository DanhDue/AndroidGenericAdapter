package com.danhdueexoictif.androidgenericadapter.ui.screen.login

import androidx.lifecycle.ViewModel
import com.danhdueexoictif.androidgenericadapter.ui.base.mvi.MVIPresenter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModel : ViewModel(), MVIPresenter<LoginViewIntent, LoginViewState> {

    private val _viewStates: MutableStateFlow<LoginViewState> =
        MutableStateFlow(LoginViewState.Idle)

    private val actionsChannel = Channel<LoginViewIntent>(Channel.UNLIMITED)

    override fun processIntent(intents: Flow<LoginViewIntent>) {
        Timber.d("processIntent(intents: $intents)")
        intents.onEach { intent ->
            when (intent) {
                LoginViewIntent.LoadInitViewIntent -> {
                    _viewStates.value = LoginViewState.Idle
                }
                LoginViewIntent.GoToForgotPasswordViewIntent -> {
                    _viewStates.value = LoginViewState.Submitting
                }
            }
        }
    }

    override val viewState: StateFlow<LoginViewState> = _viewStates
}
