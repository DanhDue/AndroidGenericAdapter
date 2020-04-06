package com.danhdueexoictif.androidgenericadapter.ui.widgets.recyclerview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.danhdueexoictif.androidgenericadapter.R
import com.danhdueexoictif.androidgenericadapter.databinding.CustomRecyclerviewBinding
import com.danhdueexoictif.androidgenericadapter.utils.extension.setUpRecyclerView
import kotlinx.android.synthetic.main.custom_recyclerview.view.*
import org.koin.core.KoinComponent
import timber.log.Timber

@BindingMethods(
    BindingMethod(
        type = CustomRecyclerView::class,
        attribute = "adapter",
        method = "setRecyclerAdapter"
    ),
    BindingMethod(
        type = CustomRecyclerView::class,
        attribute = "isMoreLoading",
        method = "isMoreLoading"
    ),
    BindingMethod(
        type = CustomRecyclerView::class,
        attribute = "isRefreshing",
        method = "setRefreshing"
    ),
    BindingMethod(
        type = CustomRecyclerView::class,
        attribute = "isEmptyView",
        method = "isEmptyView"
    ),
    BindingMethod(
        type = CustomRecyclerView::class,
        attribute = "isDecorator",
        method = "isDecorator"
    ),
    BindingMethod(
        type = CustomRecyclerView::class,
        attribute = "isLoadMoreFail",
        method = "isLoadMoreFail"
    ),
    BindingMethod(
        type = CustomRecyclerView::class,
        attribute = "loadMoreDisabled",
        method = "loadMoreDisabled"
    ),
    BindingMethod(
        type = CustomRecyclerView::class,
        attribute = "itemAnimator",
        method = "setItemAnimator"
    ),
    BindingMethod(
        type = CustomRecyclerView::class,
        attribute = "itemDecoration",
        method = "setItemDecoration"
    )
)
class CustomRecyclerView : RelativeLayout, KoinComponent {

    private var loadMoreIsDisabled = false
    private var isShowEmptyView: Boolean = true
    private var isDecorator: Boolean = false

    private var ctx: Context

    var customRecyclerViewCallback: CustomRecyclerViewCallback? = null
    private lateinit var binding: CustomRecyclerviewBinding
    private var adapterCustomRecyclerView: BaseRecyclerAdapter<*, *>? = null

    private val endlessRecyclerOnScrollListener = object : EndlessRecyclerOnScrollListener() {
        override fun onLoadMore() {
            if (!loadMoreIsDisabled) customRecyclerViewCallback?.onLoadMore()
        }
    }

//    private val animDown: Animation = AnimationUtils.loadAnimation(this.context,
//        R.anim.slide_down_loadmore
//    ).apply {
//        setAnimationListener(object : Animation.AnimationListener {
//            override fun onAnimationRepeat(animation: Animation?) {
//            }
//
//            override fun onAnimationEnd(animation: Animation?) {
//                progress_load_more.visibility = View.GONE
//            }
//
//            override fun onAnimationStart(animation: Animation?) {
//                progress_load_more.visibility = View.VISIBLE
//            }
//
//        })
//    }

    private val animUp = AnimationUtils.loadAnimation(this.context, R.anim.slide_up_loadmore)

    constructor(context: Context) : super(context) {
        ctx = context
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        ctx = context
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        ctx = context
        initView()
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        Timber.d("event.x: ${ev?.x}, event.y: ${ev?.y}")
        return super.onInterceptTouchEvent(ev)
    }

    private fun initView() {
        DataBindingUtil.inflate<CustomRecyclerviewBinding>(
            LayoutInflater.from(context),
            R.layout.custom_recyclerview, this, true
        ).apply {
            binding = this
            this.viewModel = this@CustomRecyclerView
        }

        recycler_view.apply {
            setUpRecyclerView(isDecorator)
            addOnScrollListener(endlessRecyclerOnScrollListener)
        }
        refresh.setOnRefreshListener {
            customRecyclerViewCallback?.onRefresh()
        }
    }

    fun getRecyclerView(): RecyclerView = recycler_view

    fun setRefreshing(isRefresh: Boolean) {
        refresh.isRefreshing = isRefresh
    }

    fun loadMoreDisabled(loadMoreDisabled: Boolean) {
        this.loadMoreIsDisabled = loadMoreDisabled
    }

    fun setItemAnimator(itemAnimator: RecyclerView.ItemAnimator?) {
        recycler_view.itemAnimator = itemAnimator
    }

    fun setItemDecoration(itemDecoration: RecyclerView.ItemDecoration) {
        recycler_view.addItemDecoration(itemDecoration)
    }

//    var handlerShowLoadMore = Handler()
//    var runShowLoadMore = Runnable {
//        progress_load_more.startAnimation(animDown)
//    }

    fun isMoreLoading(loadMore: Boolean) {
        if (loadMore) {
//            handlerShowLoadMore.removeCallbacks(runShowLoadMore)
            progress_load_more.visibility = View.VISIBLE
            progress_load_more.startAnimation(animUp)
        } else {
//            if ((adapterCustomRecyclerView?.itemCount ?: 0) > Constants.DEFAULT_ITEM_PER_PAGE) {
//                handlerShowLoadMore.postDelayed(runShowLoadMore, 300)
//            }
            progress_load_more.visibility = View.GONE
        }
    }

    fun setRecyclerAdapter(recyclerAdapter: BaseRecyclerAdapter<*, *>) {
        if (adapterCustomRecyclerView == null) {
            recyclerAdapter.apply {
                baseRecyclerAdapterCallBack = object : BaseRecyclerAdapterCallBack {
                    override fun itemCount(count: Int) {
                        if (count == 0 && isShowEmptyView) {
                            layout_no_data.visibility = View.VISIBLE
                        } else {
                            layout_no_data.visibility = GONE
                        }
                    }
                }
                recycler_view.adapter = this
                adapterCustomRecyclerView = this
            }
        }
    }

    fun isEmptyView(isShowEmptyView: Boolean) {
        this.isShowEmptyView = isShowEmptyView
    }

    fun isDecorator(isDecorator: Boolean) {
        this.isDecorator = isDecorator
    }


    fun setEmptyView(view: View?) {
        view?.let {
            isShowEmptyView = true
            layout_no_data.removeAllViews()
            layout_no_data.addView(it)
        } ?: run {
            isShowEmptyView = false
        }
    }

    fun resetLoadMore() {
        endlessRecyclerOnScrollListener.resetOnLoadMore()
    }

    fun isLoadMoreFail(isFail: Boolean) {
        if (isFail) {
            endlessRecyclerOnScrollListener.resetOnLoadMore()
        }
    }


    interface CustomRecyclerViewCallback {
        fun onLoadMore()
        fun onRefresh()
    }

    fun reset() {
        refresh.isEnabled = true
//        recycler_view.enableScrolling = true
    }
}
