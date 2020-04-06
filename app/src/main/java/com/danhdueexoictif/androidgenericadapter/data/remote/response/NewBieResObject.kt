package com.danhdueexoictif.androidgenericadapter.data.remote.response

import com.danhdueexoictif.androidgenericadapter.data.bean.NewBieObject
import com.danhdueexoictif.androidgenericadapter.ui.widgets.recyclerview.BaseRecyclerViewModel
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class NewBieResObject(
    @SerializedName("data")
    var data: List<NewBieObject>? = null,
    @SerializedName("error")
    var error: ErrorResponse? = null
) : BaseRecyclerViewModel()
