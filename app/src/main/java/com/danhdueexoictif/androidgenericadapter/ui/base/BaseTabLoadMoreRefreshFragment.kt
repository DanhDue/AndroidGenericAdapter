package com.danhdueexoictif.androidgenericadapter.ui.base

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.danhdueexoictif.androidgenericadapter.BR
import com.danhdueexoictif.androidgenericadapter.NavGlobalDirections
import com.danhdueexoictif.androidgenericadapter.R
import com.danhdueexoictif.androidgenericadapter.ui.widgets.recyclerview.BaseRecyclerAdapter
import com.danhdueexoictif.androidgenericadapter.ui.widgets.recyclerview.CustomRecyclerView

abstract class BaseTabLoadMoreRefreshFragment<ViewBinding : ViewDataBinding, ViewModel : BaseLoadMoreRefreshViewModel<Item>, Item> :
    BaseMainTabFragment<ViewBinding, ViewModel>() {

    abstract val customRecyclerView: CustomRecyclerView

    abstract val adapter: BaseRecyclerAdapter<Item, *>?

    override fun handleShowLoading(isLoading: Boolean) {
        if (isLoading) showLoading() else hideLoading()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewBinding.setVariable(BR.adapter, adapter)
        super.onViewCreated(view, savedInstanceState)
    }

    @CallSuper
    override fun observeField() {
        super.observeField()
        if (sharedViewModel.shouldNavigateToRoot && navController.graph.id == R.id.nav_home) {
            sharedViewModel.shouldNavigateToRoot = false
            navController.navigate(NavGlobalDirections.navigateToRoot())
        }
        setupAdapter()
        setupViewModel()
    }

    private fun setupAdapter() {
        customRecyclerView.customRecyclerViewCallback =
            object : CustomRecyclerView.CustomRecyclerViewCallback {
                override fun onRefresh() {
                    viewModel.refreshData()
                    customRecyclerView.resetLoadMore()
                }

                override fun onLoadMore() {
                    viewModel.loadMore()
                }
            }
    }

    private fun setupViewModel() {
        viewModel.apply {
            listItem.observe(viewLifecycleOwner, Observer {
                adapter?.submitList(it)
            })
            isRefreshing.observe(viewLifecycleOwner, Observer {
                if (it == true) adapter?.submitList(arrayListOf())
            })
        }
    }
}
