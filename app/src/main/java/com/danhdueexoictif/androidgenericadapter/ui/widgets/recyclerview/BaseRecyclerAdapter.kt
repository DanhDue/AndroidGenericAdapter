package com.danhdueexoictif.androidgenericadapter.ui.widgets.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import java.util.concurrent.Executors

abstract class BaseRecyclerAdapter<Item, ViewBinding : ViewDataBinding>(
    callBack: DiffUtil.ItemCallback<Item>
) : ListAdapter<Item, BaseViewHolder<out ViewBinding, Item>>(
    AsyncDifferConfig.Builder<Item>(callBack)
        .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor())
        .build()
) {
    protected abstract fun getViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int
    ): BaseViewHolder<out ViewBinding, Item>

    var baseRecyclerAdapterCallBack: BaseRecyclerAdapterCallBack? = null

    override fun submitList(list: List<Item>?) {
        baseRecyclerAdapterCallBack?.itemCount(list?.size ?: 0)
        super.submitList(ArrayList<Item>(list ?: listOf()))
    }

    fun copyItems(): MutableList<Item> {
        val result = mutableListOf<Item>()
        result.addAll(currentList)
        return result
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        getViewHolder(parent, LayoutInflater.from(parent.context), viewType)

    override fun onBindViewHolder(holder: BaseViewHolder<out ViewBinding, Item>, position: Int) {
        holder.onBindData(getItem(position), position)
        holder.binding.executePendingBindings()
    }

    open fun refreshData() {}
}

interface BaseRecyclerAdapterCallBack {
    fun itemCount(count: Int)
}
