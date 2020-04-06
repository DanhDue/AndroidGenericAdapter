package com.danhdueexoictif.androidgenericadapter.ui.screen.menu

import com.danhdueexoictif.androidgenericadapter.R
import com.danhdueexoictif.androidgenericadapter.databinding.MenuFragmentBinding
import com.danhdueexoictif.androidgenericadapter.ui.base.BaseFragment
import org.koin.android.ext.android.inject

class MenuFragment : BaseFragment<MenuFragmentBinding, MenuViewModel>() {

    companion object {
        fun newInstance() = MenuFragment()
    }

    override val layoutId: Int = R.layout.menu_fragment
    override val viewModel: MenuViewModel by inject()

}
