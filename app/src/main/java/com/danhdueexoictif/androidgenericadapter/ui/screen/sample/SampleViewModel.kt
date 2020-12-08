package com.danhdueexoictif.androidgenericadapter.ui.screen.sample

import androidx.lifecycle.viewModelScope
import com.danhdueexoictif.androidgenericadapter.R
import com.danhdueexoictif.androidgenericadapter.data.remote.invoke
import com.danhdueexoictif.androidgenericadapter.data.repository.SampleRepository
import com.danhdueexoictif.androidgenericadapter.ui.base.BaseLoadMoreRefreshViewModel
import com.danhdueexoictif.androidgenericadapter.ui.widgets.recyclerview.BaseRecyclerViewModel
import com.danhdueexoictif.androidgenericadapter.utils.Constants
import kotlinx.coroutines.launch
import timber.log.Timber

class SampleViewModel(private val sampleRepo: SampleRepository) :
    BaseLoadMoreRefreshViewModel<BaseRecyclerViewModel>() {

    init {
        firstLoad()
    }

    override fun loadData(page: Int) {
        Timber.d("loadData(page: $page)")
        viewModelScope.launch {
            val member = sampleRepo.getNewbies(Constants.DEFAULT_FIRST_PAGE).await()
            member()?.let {
                it.data?.get(0)?.apply { layoutId = R.layout.item_first_newbie }
                onLoadSuccess(it.data)
            } ?: apply {
                member.handleErrors()
            }
        }
    }

    fun saveUserName() {
        sampleRepo.saveUserName()
    }

    fun getUserName(): String? = sampleRepo.getUserName()
}
