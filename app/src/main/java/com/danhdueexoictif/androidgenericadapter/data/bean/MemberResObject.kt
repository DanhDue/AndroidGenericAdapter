package com.danhdueexoictif.androidgenericadapter.data.bean

import com.google.gson.annotations.SerializedName

data class MemberResObject(
    @SerializedName("required_point")
    val requiredPoint: String?, // Number of Points required to move to the next Tier
    @SerializedName("advocates_flg")
    val advocatesFlg: String?,
    @SerializedName("balance")
    val balance: String?, // Number of points earned
    @SerializedName("member_id")
    val memberId: String?, // Brand Member ID => Generate to barcode.
    @SerializedName("has_exists")
    val hasExists: String?, // The uuid has exists.
    @SerializedName("rank")
    val rank: String?, // User rank
    @SerializedName("first_name")
    val firstName: String?, // First name
    @SerializedName("point")
    val point: String?, // Point
    @SerializedName("last_name")
    val lastName: String? // Last name
)
