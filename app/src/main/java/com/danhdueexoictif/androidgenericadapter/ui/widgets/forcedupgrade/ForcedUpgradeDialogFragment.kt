package com.danhdueexoictif.androidgenericadapter.ui.widgets.forcedupgrade

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.danhdueexoictif.androidgenericadapter.BuildConfig
import com.danhdueexoictif.androidgenericadapter.R
import com.danhdueexoictif.androidgenericadapter.databinding.ForcedUpgradeDialogFragmentBinding
import com.danhdueexoictif.androidgenericadapter.ui.base.BaseDialogFragment
import com.danhdueexoictif.androidgenericadapter.utils.setSingleClick
import kotlinx.android.synthetic.main.forced_upgrade_dialog_fragment.*
import org.koin.androidx.viewmodel.ext.viewModel

class ForcedUpgradeDialogFragment :
    BaseDialogFragment<ForcedUpgradeDialogFragmentBinding, ForcedUpgradeDialogViewModel>() {

    companion object {
        fun newInstance() = ForcedUpgradeDialogFragment()
    }

    override val layoutId: Int = R.layout.forced_upgrade_dialog_fragment

    override val viewModel: ForcedUpgradeDialogViewModel by viewModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        agree_button.setSingleClick { openAppInStore() }
    }

    private fun openAppInStore() {
        val intent = Intent(
            Intent.ACTION_VIEW, Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)
        )
        startActivity(intent)
    }

}
