package com.danhdueexoictif.androidgenericadapter.ui.screen.login

import com.danhdueexoictif.androidgenericadapter.R
import com.danhdueexoictif.androidgenericadapter.databinding.LoginFragmentBinding
import com.danhdueexoictif.androidgenericadapter.ui.base.BaseFragment
import org.koin.android.ext.android.inject

class LoginFragment : BaseFragment<LoginFragmentBinding, LoginViewModel>() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    override val layoutId: Int = R.layout.login_fragment
    override val viewModel: LoginViewModel by inject()

}
