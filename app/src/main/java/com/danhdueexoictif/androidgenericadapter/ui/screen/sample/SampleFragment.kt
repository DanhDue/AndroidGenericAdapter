package com.danhdueexoictif.androidgenericadapter.ui.screen.sample

import android.os.Bundle
import com.danhdueexoictif.androidgenericadapter.R
import com.danhdueexoictif.androidgenericadapter.data.bean.NewBieObject
import com.danhdueexoictif.androidgenericadapter.databinding.SampleFragmentBinding
import com.danhdueexoictif.androidgenericadapter.ui.base.BaseMainTabLoadMoreRefreshFragment
import com.danhdueexoictif.androidgenericadapter.ui.widgets.recyclerview.BaseRecyclerAdapter
import com.danhdueexoictif.androidgenericadapter.ui.widgets.recyclerview.BaseRecyclerViewModel
import com.danhdueexoictif.androidgenericadapter.ui.widgets.recyclerview.CustomRecyclerView
import com.danhdueexoictif.androidgenericadapter.utils.Constants
import kotlinx.android.synthetic.main.sample_fragment.*
import org.koin.android.ext.android.inject

class SampleFragment :
    BaseMainTabLoadMoreRefreshFragment<SampleFragmentBinding, SampleViewModel, BaseRecyclerViewModel>() {

    companion object {
        fun newInstance() = SampleFragment()
        const val SCREEN_NAME = "SampleFragment"
    }

    override val layoutId: Int = R.layout.sample_fragment
    override val viewModel: SampleViewModel by inject()

    override val customRecyclerView: CustomRecyclerView
        get() = recSamples

    override val adapter: BaseRecyclerAdapter<BaseRecyclerViewModel, *>? by lazy {
        GenericAdapter(NewBieObject.COMPARATOR).apply { eventController = this@SampleFragment }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loggingHelper.recordFirebaseScreenView(requireActivity(), SCREEN_NAME, null)
        loggingHelper.logEvent(
            SCREEN_NAME,
            Constants.LoggingParam.INIT_VALUE,
            Constants.LoggingParam.MEMBER_CATEGORY
        )
    }
}
