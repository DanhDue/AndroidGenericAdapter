package com.danhdueexoictif.androidgenericadapter.ui.widgets.recyclerview

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.danhdueexoictif.androidgenericadapter.BR

abstract class BaseViewHolder<V : ViewDataBinding, Item> constructor(val binding: V) :
    RecyclerView.ViewHolder(binding.root) {

    protected var item: Item? = null

    fun onBindData(item: Item, position: Int) {
        binding.setVariable(BR.viewModel, this)
        binding.setVariable(BR.item, item)
        this.item = item
        bindData(item, position)
    }

    abstract fun bindData(item: Item, position: Int)
}
