package com.danhdueexoictif.androidgenericadapter.data.bean

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.danhdueexoictif.androidgenericadapter.R
import com.danhdueexoictif.androidgenericadapter.ui.widgets.recyclerview.BaseRecyclerViewModel
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NewBieObject(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("fullname")
    val fullname: String? = null,
    @SerializedName("position")
    val position: String? = null,
    @SerializedName("location")
    val location: String? = null,
    @SerializedName("dob")
    val dob: String? = null,
    @SerializedName("quote")
    val quote: String? = null,
    @SerializedName("image_uuid")
    val imageUuid: String? = null,
    @SerializedName("join_at")
    val joinAt: String? = null,
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("avatarMember")
    val avatarMember: AvatarMember? = null,
    @SerializedName("hobby")
    val hobby: String? = null,
    @SerializedName("workspace")
    val workspace: String? = null,
    @SerializedName("dislike")
    val dislike: String? = null,
    @Transient
    override var layoutId: Int = R.layout.item_newbie
) : BaseRecyclerViewModel() {

    @Parcelize
    data class AvatarMember(
        @SerializedName("data") val data: Data? = null
    ) : Parcelable {

        @Parcelize
        data class Data(
            @SerializedName("uuid") val uuid: String? = null,
            @SerializedName("origin_path") val originPath: String? = null,
            @SerializedName("thumbnail_path") val thumbnailPath: String? = null
        ) : Parcelable
    }

    companion object {

        val COMPARATOR = object : DiffUtil.ItemCallback<BaseRecyclerViewModel>() {
            override fun areItemsTheSame(
                oldItem: BaseRecyclerViewModel, newItem: BaseRecyclerViewModel
            ): Boolean = false

            override fun areContentsTheSame(
                oldItem: BaseRecyclerViewModel, newItem: BaseRecyclerViewModel
            ): Boolean = false
        }
    }
}
