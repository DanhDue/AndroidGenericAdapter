package com.danhdueexoictif.androidgenericadapter.data.bean

import com.google.gson.annotations.SerializedName

data class AccountObject(
    val id: Int? = null,
    val name: String? = null,
    val username: String? = null,
    val email: String? = null,
    val avatar: String? = null,
    val language: String? = null,
    @SerializedName("is_company_member") val isCompanyMember: Boolean? = null,
    @SerializedName("created_at") val createdAt: String? = null,
    @SerializedName("updated_at") val updatedAt: String? = null,
    @SerializedName("deleted_at") val deletedAt: String? = null
)
