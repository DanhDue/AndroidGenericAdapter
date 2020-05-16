package com.danhdueexoictif.androidgenericadapter.utils

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.SystemClock
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.databinding.InverseBindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.danhdueexoictif.androidgenericadapter.BuildConfig
import com.danhdueexoictif.androidgenericadapter.ui.widgets.AdvancedWebView


@BindingAdapter("onRefreshListener")
fun SwipeRefreshLayout.customRefreshListener(listener: SwipeRefreshLayout.OnRefreshListener?) {
    if (listener != null) setOnRefreshListener(listener)
}

@BindingAdapter("isRefreshing")
fun SwipeRefreshLayout.customRefreshing(refreshing: Boolean?) {
    isRefreshing = refreshing == true
}

@BindingAdapter("onScrollListener")
fun RecyclerView.customScrollListener(listener: RecyclerView.OnScrollListener?) {
    if (listener != null) addOnScrollListener(listener)
}

@BindingAdapter("glideSrc")
fun ImageView.setGlideSrc(@DrawableRes src: Int?) {
    Glide.with(context).load(src).into(this)
}

@BindingAdapter("loadUri")
fun ImageView.loadLocalImage(uri: Uri?) {
    Glide.with(context).load(uri).into(this)
}

@BindingAdapter(value = ["loadImageLocal"])
fun ImageView.loadLocalImage(imageName: String?) {
    if (TextUtils.isEmpty(imageName).not()) {
        Glide.with(context)
            .load(resources.getIdentifier(imageName, "drawable", BuildConfig.APPLICATION_ID))
            .into(this)
    }
}

@BindingAdapter(
    value = ["localImageId", "centerCrop", "circleCrop"], requireAll = false
)
fun ImageView.loadLocalImageById(
    @DrawableRes imageId: Int?, isCenterCrop: Boolean = false, isCircleCrop: Boolean = false
) {
    imageId?.let {
        val requestBuilder = Glide.with(context).load(it)
        if (isCenterCrop) requestBuilder.centerCrop()
        if (isCircleCrop) requestBuilder.circleCrop()
        requestBuilder.into(this)
    }
}

/**
 * load image from image url using glide
 * @param imageUrl string url of image
 * @param thumbnailUrl string url of thumbnail image
 * @param placeHolder drawable for loading
 * @param errorDrawable drawable for error case
 * @param requestListener listener for loading success or fail
 * @param centerCrop center crop flag
 * @param fitCenter fit center flag
 * @param circleCrop circle crop flag
 * @param diskCacheStrategy cache stragegy, check below comment for detail
 * @param signatureKey key for checking same url but image changed
 */
@BindingAdapter(
    value = ["imageUrl", "thumbnailUrl", "placeholder", "errorDrawable", "requestListener", "centerCrop", "fitCenter", "circleCrop", "diskCacheStrategy", "signatureKey"],
    requireAll = false
)
fun ImageView.loadImage(
    imageUrl: String? = null,
    thumbnailUrl: String? = null,
    placeHolder: Drawable? = null,
    errorDrawable: Drawable? = null,
    requestListener: RequestListener<Drawable>? = null,
    centerCrop: Boolean = false,
    fitCenter: Boolean = false,
    circleCrop: Boolean = false,
    diskCacheStrategy: DiskCacheStrategy? = null,
    signatureKey: String? = null
) {
    if (imageUrl.isNullOrBlank()) {
        setImageDrawable(placeHolder)
        return
    }

    val requestOptions = RequestOptions().apply {

        // cache type: https://futurestud.io/tutorials/glide-how-to-choose-the-best-caching-preference
        // Glide 4.x: DiskCacheStrategy.RESOURCE Glide 3.x: DiskCacheStrategy.RESULT caches only the final image, after reducing the resolution (and possibly transformations) (default behavior of Glide 3.x)
        // Glide 4.x: DiskCacheStrategy.DATA, Glide 3.x: DiskCacheStrategy.SOURCE caches only the original full-resolution image
        // Glide 4.x only: DiskCacheStrategy.AUTOMATIC intelligently chooses a cache strategy based on the resource (default behavior of Glide 4.x)
        // Glide 3.x & 4.x: DiskCacheStrategy.ALL caches all versions of the image
        // Glide 3.x & 4.x: DiskCacheStrategy.NONE caches nothing
        diskCacheStrategy(diskCacheStrategy ?: DiskCacheStrategy.RESOURCE)

        placeholder(placeHolder)
        error(errorDrawable)

        // crop type
        if (centerCrop) centerCrop()
        if (fitCenter) fitCenter()
        if (circleCrop) circleCrop()

        if (!signatureKey.isNullOrBlank()) {
            signature(ObjectKey(signatureKey))
        }
    }

    GlideApp.with(context).load(imageUrl).apply {
        transition(DrawableTransitionOptions.withCrossFade())
        addListener(requestListener)
        // breaks sharedElementEnterTransition
        if (thumbnailUrl.isNullOrBlank().not()) {
            thumbnail(Glide.with(context).load(thumbnailUrl).apply(requestOptions))
        } else {
            thumbnail(0.2f)
        }
        apply(requestOptions)
    }.into(this)
}

@BindingAdapter("clickSafe")
fun View.setClickSafe(listener: View.OnClickListener?) {
    setOnClickListener(object : View.OnClickListener {
        var lastClickTime: Long = 0

        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < Constants.THRESHOLD_CLICK_TIME) {
                return
            }
            listener?.onClick(v)
            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}

@BindingAdapter("touchListener")
fun setTouchListener(self: View, value: Boolean) {
    self.setOnTouchListener { view, event ->
        when (event.action) {
            // Check if the button is PRESSED
            MotionEvent.ACTION_DOWN -> {
                //do some thing
            }
            // Check if the button is RELEASED
            MotionEvent.ACTION_UP -> {
                //do some thing
            }
        }
        false
    }
}

@BindingAdapter("onSingleTouch")
@SuppressLint("ClickableViewAccessibility")
fun View.setSingleTouch(execution: () -> Unit) {
    setOnTouchListener(object : View.OnTouchListener {
        var lastClickTime: Long = 0
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            if (SystemClock.elapsedRealtime() - lastClickTime < Constants.THRESHOLD_CLICK_TIME) {
                return false
            }
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    alpha = 0.5f
                    return true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    alpha = 1f
                    execution.invoke()
                    lastClickTime = SystemClock.elapsedRealtime()
                    return true
                }
            }
            return false
        }
    })
}

@BindingAdapter("onSingleClick")
fun View.setSingleClick(execution: () -> Unit) {
    setOnClickListener(object : View.OnClickListener {
        var lastClickTime: Long = 0

        override fun onClick(p0: View?) {
            if (SystemClock.elapsedRealtime() - lastClickTime < Constants.THRESHOLD_CLICK_TIME) {
                return
            }
            lastClickTime = SystemClock.elapsedRealtime()
            execution.invoke()
        }
    })
}

@BindingAdapter("loadUrl")
fun AdvancedWebView.loadUrl(url: String?) {
    url?.apply {
        setBackgroundColor(android.graphics.Color.BLACK)
        loadUrl(url)
    }
}

@BindingAdapter("loadHtml")
fun AdvancedWebView.loadHtml(html: String?) {
    html?.let {
        setBackgroundColor(android.graphics.Color.BLACK)
        loadHtml(it)
    }
}

@BindingAdapter("backgroundTint")
fun TextView.customBackgroundTint(color: Int?) {
    if (color == null) return
    background?.setTint(color)
}

@BindingAdapter("tint")
fun ImageView.customTint(color: Int?) {
    if (color == null) return
    imageTintList = ColorStateList.valueOf(color)
}

@BindingConversion
fun longToStr(value: Long?): String? {
    return value?.toString() ?: ""
}

@InverseBindingAdapter(attribute = "android:text", event = "android:textAttrChanged")
fun captureLongValue(view: EditText): Long? {
    var value: Long = 0
    try {
        value = view.text.toString().toLong()
    } catch (e: NumberFormatException) {
        e.printStackTrace()
    }
    return value
}

@BindingAdapter("goneIfTextIsNullOrBlank")
fun View.gone(startDate: String?) {
    visibility = if (startDate.isNullOrBlank()) View.GONE else View.VISIBLE
}

@BindingAdapter("visibilityIfTextIsNullOrBlank")
fun View.visibility(startDate: String?) {
    visibility = if (startDate.isNullOrBlank()) View.VISIBLE else View.GONE
}
