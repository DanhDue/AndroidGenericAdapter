package com.danhdueexoictif.androidgenericadapter.ui.widgets.recyclerview

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danhdueexoictif.androidgenericadapter.utils.Constants
import com.danhdueexoictif.androidgenericadapter.utils.extension.safeLog

abstract class EndlessRecyclerOnScrollListener(
    threshold: Int = Constants.DEFAULT_NUM_VISIBLE_THRESHOLD
) : RecyclerView.OnScrollListener() {

    // The total number of items in the dataset after the last load
    private var previousTotal: Int = 0
    private var isLoading = true
    private var firstVisibleItem: Int = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0
    private var numberThreshold: Int = -1

    init {
        numberThreshold = if (threshold >= 1) {
            threshold
        } else {
            Constants.DEFAULT_NUM_VISIBLE_THRESHOLD
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (dy < 0) return
        visibleItemCount = recyclerView.childCount
        totalItemCount = recyclerView.layoutManager?.itemCount ?: 0
        when (val layoutManager = recyclerView.layoutManager) {
            is LinearLayoutManager -> {
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
            }
            is GridLayoutManager -> {
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
            }
            else -> {
                Exception("Unsupported LayoutManage").safeLog()
            }
        }

        if (isLoading && totalItemCount != previousTotal) {
            isLoading = false
            previousTotal = totalItemCount
        }

        if (!isLoading && totalItemCount - visibleItemCount <= firstVisibleItem + numberThreshold) {
            onLoadMore()
            isLoading = true
        }
    }

    fun resetOnLoadMore() {
        previousTotal = 0
        isLoading = true
    }

    abstract fun onLoadMore()
}
