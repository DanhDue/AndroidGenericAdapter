package com.danhdueexoictif.androidgenericadapter.ui.widgets.recyclerview

import android.os.Parcelable
import com.danhdueexoictif.androidgenericadapter.utils.controller.OnEventController
import com.google.gson.annotations.SerializedName

abstract class BaseRecyclerViewModel(
    @SerializedName("item_id")
    open var itemId: Long? = 0,
    @SerializedName("layout_id")
    open var layoutId: Int = -1,
    @SerializedName("adapter_position")
    var adapterPosition: Int = -1,
    @SerializedName("eventController")
    var eventController: OnEventController? = null,
    @SerializedName("localEventController")
    var localEventController: OnEventController? = null,
    @SerializedName("isBottom")
    var isBottom: Boolean? = false
) : Parcelable
