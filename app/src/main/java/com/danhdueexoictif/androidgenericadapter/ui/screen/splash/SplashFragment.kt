package com.danhdueexoictif.androidgenericadapter.ui.screen.splash

import android.os.Bundle
import android.os.Handler
import com.danhdueexoictif.androidgenericadapter.R
import com.danhdueexoictif.androidgenericadapter.databinding.SplashFragmentBinding
import com.danhdueexoictif.androidgenericadapter.ui.base.BaseFragment
import org.koin.android.ext.android.inject

class SplashFragment : BaseFragment<SplashFragmentBinding, SplashViewModel>() {

    companion object {
        fun newInstance() = SplashFragment()
    }

    override val layoutId: Int = R.layout.splash_fragment
    override val viewModel: SplashViewModel by inject()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Handler().postDelayed({
            navController.navigate(R.id.sampleDest)
        }, 1500)
    }

}
