package com.danhdueexoictif.androidgenericadapter.ui.widgets.oopsnointernet

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.lifecycle.Observer
import com.danhdueexoictif.androidgenericadapter.R
import com.danhdueexoictif.androidgenericadapter.databinding.OopsNoInternetDialogFragmentBinding
import com.danhdueexoictif.androidgenericadapter.ui.base.BaseDialogFragment
import com.danhdueexoictif.androidgenericadapter.utils.networkdetection.NetworkDetection
import com.danhdueexoictif.androidgenericadapter.utils.networkdetection.NoInternetUtils
import com.danhdueexoictif.androidgenericadapter.utils.setSingleTouch
import kotlinx.android.synthetic.main.oops_no_internet_dialog_fragment.*
import org.koin.androidx.viewmodel.ext.viewModel
import timber.log.Timber

class OopsNoInternetDialogFragment :
    BaseDialogFragment<OopsNoInternetDialogFragmentBinding, OopsNoInternetDialogViewModel>() {

    private val airPlanModeIntentFilter = IntentFilter("android.intent.action.AIRPLANE_MODE")

    private val airPlaneModeReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Timber.d("AirplaneMode is changed.")
            viewModel.airPlanIsOn.value = NoInternetUtils.isAirplaneModeOn(requireContext())
        }
    }

    companion object {
        fun newInstance() = OopsNoInternetDialogFragment()
    }

    override val layoutId: Int = R.layout.oops_no_internet_dialog_fragment

    override val viewModel: OopsNoInternetDialogViewModel by viewModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
    }

    override fun observeField() {
        super.observeField()
        NetworkDetection(requireActivity().application).also {
            it.observe(viewLifecycleOwner, Observer { connectionState ->
                /* every time connection state changes, we'll be notified and can perform action accordingly */
                if (connectionState.isConnected) {
                    networkIsActive()
                } else {
                    Timber.d(getString(R.string.connection_is_turned_off))
                    networkIsInActive()
                }
            })
        }
        requireContext().registerReceiver(airPlaneModeReceiver, airPlanModeIntentFilter)
    }

    override fun onDestroyView() {
        requireContext().unregisterReceiver(airPlaneModeReceiver)
        super.onDestroyView()
    }

    private fun initViews() {
        viewModel.airPlanIsOn.value = NoInternetUtils.isAirplaneModeOn(requireContext())
        butOpenWifiSettings?.setSingleTouch { NoInternetUtils.turnOnWifi(requireContext()) }
        butOpenMobileDataSettings?.setSingleTouch { NoInternetUtils.turnOnMobileData(requireContext()) }
        butOpenAirPlaneSettings?.setSingleTouch { NoInternetUtils.turnOffAirplaneMode(requireContext()) }
    }

    private fun networkIsActive() {
        dismiss()
    }

    private fun networkIsInActive() {
        Timber.d("networkIsInActive()")
    }
}
