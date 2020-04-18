package com.danhdueexoictif.androidgenericadapter.ui.screen.sample

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.danhdueexoictif.androidgenericadapter.ui.widgets.recyclerview.BaseRecyclerAdapter
import com.danhdueexoictif.androidgenericadapter.ui.widgets.recyclerview.BaseRecyclerViewModel
import com.danhdueexoictif.androidgenericadapter.ui.widgets.recyclerview.BaseViewHolder
import com.danhdueexoictif.androidgenericadapter.utils.controller.OnEventController

val workCallback = object : DiffUtil.ItemCallback<BaseRecyclerViewModel>() {
    override fun areItemsTheSame(
        oldItem: BaseRecyclerViewModel,
        newItem: BaseRecyclerViewModel
    ): Boolean =
        oldItem.itemId == newItem.itemId

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: BaseRecyclerViewModel,
        newItem: BaseRecyclerViewModel
    ): Boolean = false
}

class GenericAdapter(callBack: DiffUtil.ItemCallback<BaseRecyclerViewModel> = workCallback) :
    BaseRecyclerAdapter<BaseRecyclerViewModel, ViewDataBinding>(callBack) {

    var eventController: OnEventController? = null

    override fun getViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int
    ): BaseViewHolder<out ViewDataBinding, BaseRecyclerViewModel> {
        return GenericViewHolder(
            DataBindingUtil.inflate(inflater, viewType, parent, false)
        )
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).layoutId
    }

    inner class GenericViewHolder(binding: ViewDataBinding) :
        BaseViewHolder<ViewDataBinding, BaseRecyclerViewModel>(binding) {
        override fun bindData(item: BaseRecyclerViewModel, position: Int) {
            item.adapterPosition = position
            eventController?.let { item.eventController = it }
        }
    }
}
