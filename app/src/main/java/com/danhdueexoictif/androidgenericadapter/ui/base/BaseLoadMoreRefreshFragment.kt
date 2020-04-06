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

abstract class BaseLoadMoreRefreshFragment<ViewBinding : ViewDataBinding, ViewModel : BaseLoadMoreRefreshViewModel<Item>, Item> :
    BaseFragment<ViewBinding, ViewModel>() {

    abstract val customRecyclerView: CustomRecyclerView

    abstract val adapter: BaseRecyclerAdapter<Item, *>?

    override fun handleShowLoading(isLoading: Boolean) {
        if (isLoading) showLoading() else hideLoading()
    }

    @CallSuper
    override fun observeField() {
        if (sharedViewModel.shouldNavigateToRoot && navController.graph.id == R.id.nav_home) {
            sharedViewModel.shouldNavigateToRoot = false
            navController.navigate(NavGlobalDirections.navigateToRoot())
        }
        viewModel.apply {
            listItem.observe(viewLifecycleOwner, Observer { items ->
                adapter?.submitList(items)
                submitListDone()
            })
            isRefreshing.observe(viewLifecycleOwner, Observer { isRefreshing ->
                if (isRefreshing == true) {
                    adapter?.refreshData()
                    adapter?.submitList(arrayListOf())
                }
            })
            isDataSetChanged.observe(viewLifecycleOwner, Observer { isDataSetChanged ->
                if (isDataSetChanged == true) {
                    adapter?.notifyDataSetChanged()
                }
            })
            isItemDataSetChanged.observe(viewLifecycleOwner, Observer { position ->
                adapter?.notifyItemChanged(position)
            })
        }
    }

    open fun submitListDone() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewBinding.setVariable(BR.adapter, adapter)
        super.onViewCreated(view, savedInstanceState)
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
}
