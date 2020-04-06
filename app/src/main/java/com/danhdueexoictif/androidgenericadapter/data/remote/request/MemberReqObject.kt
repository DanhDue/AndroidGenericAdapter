package com.danhdueexoictif.androidgenericadapter.data.remote.request

import com.google.gson.annotations.SerializedName

data class MemberReqObject(
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("site_serial")
    val siteSerial: String,
    @SerializedName("d3t3")
    val d3t3: String?
)
