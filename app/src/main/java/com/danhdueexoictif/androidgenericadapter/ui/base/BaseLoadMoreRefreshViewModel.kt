package com.danhdueexoictif.androidgenericadapter.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.danhdueexoictif.androidgenericadapter.data.remote.NetworkResponse
import com.danhdueexoictif.androidgenericadapter.utils.Constants
import timber.log.Timber

abstract class BaseLoadMoreRefreshViewModel<Item> : BaseViewModel() {

    private var hasLoadMore = false
    private var isLoadMore = false
    var currentPage = Constants.DEFAULT_FIRST_PAGE

    val listItem = MutableLiveData<ArrayList<Item>>()
    val isRefreshing = MutableLiveData<Boolean>(false)
    val isDataSetChanged = MutableLiveData<Boolean>(false)
    val isItemDataSetChanged = MutableLiveData<Int>()
    val isShowNoResult = MutableLiveData<Boolean>(false)
    val isShowReload = MutableLiveData<Boolean>(false)
    val isLoadSuccess = MutableLiveData<Boolean>(false)
    val isEmptyList = isLoadSuccess.map { it && listItem.value?.isNullOrEmpty() ?: false }

    fun firstLoad(showShimmer: Boolean = true) {
        isShowShimmer.value = showShimmer
        loadData(Constants.DEFAULT_FIRST_PAGE)
    }

    fun loadMore() {
        Timber.d("loadMore()")
        if (!hasLoadMore || isLoadMore || isLoading.value == true || isRefreshing.value == true) {
            return
        }
        isLoadMore = true
        loadMoreData()
    }

    /**
     * override if need call other api
     */
    open fun loadMoreData() {
        currentPage = currentPage.plus(1)
        loadData(currentPage)
    }

    open fun refreshData() {
        if (isRefreshing.value == true || isLoading.value == true) {
            return
        }
        isShowNoResult.value = false
        isRefreshing.value = true
        listItem.value = null
        currentPage = Constants.DEFAULT_FIRST_PAGE
        firstLoad()
    }

    override fun reLoadData() {
        isShowReload.value = false
        listItem.value = null
        currentPage = Constants.DEFAULT_FIRST_PAGE
        firstLoad()
    }

    abstract fun loadData(page: Int)

    /**
     * override if need change number item per page
     */
    open fun getNumberItemPerPage() = Constants.DEFAULT_ITEM_PER_PAGE

    open fun onLoadSuccess(items: List<Item>?) {
        isLoadSuccess.value = true
        items?.let {
            val currentItems = listItem.value ?: arrayListOf()
            listItem.value = currentItems.apply { addAll(it) }
        }
        hasLoadMore = items?.size ?: 0 >= getNumberItemPerPage()
        isLoadMore = false
        isLoading.value = false
        isRefreshing.value = false
        if (currentPage == Constants.DEFAULT_FIRST_PAGE) {
            isShowShimmer.value = false
        }
        isShowNoResult.value = listItem.value.isNullOrEmpty()
    }

    protected fun <T : Any, U : Any> NetworkResponse<T, U>.handleErrors() {
        if (currentPage == Constants.DEFAULT_FIRST_PAGE) {
            isShowShimmer.value = false
            isShowReload.value = true
            listItem.value = null
            isLoadSuccess.value = false
        }
        isShowNoResult.value = false
        isLoading.value = false
        isRefreshing.value = false
        isLoadMore = false
        currentPage = currentPage.minus(1)
        this.handleBaseErrors()
    }
}
