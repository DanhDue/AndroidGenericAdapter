package com.danhdueexoictif.androidgenericadapter.data.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OauthToken(
    @SerializedName("token_type") var tokenType: String? = null,
    @SerializedName("expires_in") var expiresIn: Long? = null,
    @SerializedName("refresh_token") var refreshToken: String? = null,
    @SerializedName("access_token") var accessToken: String? = null,
    @SerializedName("error") var error: String? = null,
    @SerializedName("message") var message: String? = null
) : Parcelable
