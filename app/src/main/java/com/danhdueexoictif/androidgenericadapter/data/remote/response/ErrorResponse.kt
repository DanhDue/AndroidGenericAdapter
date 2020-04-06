package com.danhdueexoictif.androidgenericadapter.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ErrorResponse(
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("error_code")
    val errorCode: Int
) : Parcelable
