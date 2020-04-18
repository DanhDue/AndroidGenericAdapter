package com.danhdueexoictif.androidgenericadapter.data.bean

import com.google.gson.annotations.SerializedName

data class MemberResObject(
    @SerializedName("required_point")
    val requiredPoint: String? = null, // Number of Points required to move to the next Tier
    @SerializedName("advocates_flg")
    val advocatesFlg: String? = null,
    @SerializedName("balance")
    val balance: String? = null, // Number of points earned
    @SerializedName("member_id")
    val memberId: String? = null, // Brand Member ID => Generate to barcode.
    @SerializedName("has_exists")
    val hasExists: String? = null, // The uuid has exists.
    @SerializedName("rank")
    val rank: String? = null, // User rank
    @SerializedName("first_name")
    val firstName: String? = null, // First name
    @SerializedName("point")
    val point: String? = null, // Point
    @SerializedName("last_name")
    val lastName: String? = null // Last name
)
