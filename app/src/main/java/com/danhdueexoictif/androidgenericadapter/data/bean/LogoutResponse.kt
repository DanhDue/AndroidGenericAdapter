package com.danhdueexoictif.androidgenericadapter.data.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LogoutResponse(
    @SerializedName("message")
    val message: String? = null
) : Parcelable
