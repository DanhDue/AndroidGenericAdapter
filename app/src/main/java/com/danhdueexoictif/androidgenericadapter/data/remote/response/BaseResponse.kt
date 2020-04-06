package com.danhdueexoictif.androidgenericadapter.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
open class BaseResponse<out Item : Parcelable>(
    @SerializedName("data")
    val data: Item? = null,
    @SerializedName("error")
    val error: ErrorResponse? = null
) : Parcelable
