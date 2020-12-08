package com.danhdueexoictif.androidgenericadapter.ui.screen.login

import androidx.fragment.app.Fragment
import com.danhdueexoictif.androidgenericadapter.R
import com.danhdueexoictif.androidgenericadapter.databinding.LoginFragmentBinding
import com.danhdueexoictif.androidgenericadapter.ui.base.mvi.MVIView
import com.danhdueexoictif.androidgenericadapter.ui.base.mvi.ViewIntent
import com.danhdueexoictif.androidgenericadapter.ui.base.mvi.ViewState
import com.danhdueexoictif.androidgenericadapter.utils.viewBinding.viewBinding
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class LoginFragment : Fragment(R.layout.login_fragment), MVIView<ViewIntent, ViewState> {

    companion object {
        fun newInstance() = LoginFragment()
    }

    val viewModel: LoginViewModel by viewModel()
    val binding: LoginFragmentBinding by viewBinding(LoginFragmentBinding::bind)

    private fun LoginFragmentBinding.renderNetworkErrorsState(state: ViewState) {
        Timber.d("LoginFragmentBinding.renderNetworkErrorsState(state: $state)")
    }

    private fun LoginFragmentBinding.renderLoadingState(state: ViewState) {
        Timber.d("LoginFragmentBinding.renderLoadingState(state: $state)")
    }

    override fun render(state: ViewState) {
        TODO("Not yet implemented")
    }

    override val intents: Flow<ViewIntent>
        get() = TODO("Not yet implemented")
}
