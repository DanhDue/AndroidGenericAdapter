package com.danhdueexoictif.androidgenericadapter.ui.screen.sample

import com.danhdueexoictif.androidgenericadapter.R
import com.danhdueexoictif.androidgenericadapter.databinding.SampleFragmentBinding
import com.danhdueexoictif.androidgenericadapter.ui.base.BaseFragment
import org.koin.android.ext.android.inject

class SampleFragment : BaseFragment<SampleFragmentBinding, SampleViewModel>() {

    companion object {
        fun newInstance() = SampleFragment()
    }

    override val layoutId: Int = R.layout.sample_fragment
    override val viewModel: SampleViewModel by inject()

}
