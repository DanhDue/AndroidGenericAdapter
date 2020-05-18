package com.danhdueexoictif.androidgenericadapter.data.repository.impl

import com.danhdueexoictif.androidgenericadapter.BuildConfig
import com.danhdueexoictif.androidgenericadapter.data.bean.MemberResObject
import com.danhdueexoictif.androidgenericadapter.data.local.pref.SharedPreferenceHelper
import com.danhdueexoictif.androidgenericadapter.data.remote.ApiService
import com.danhdueexoictif.androidgenericadapter.data.remote.NetworkResponse
import com.danhdueexoictif.androidgenericadapter.data.remote.request.MemberReqObject
import com.danhdueexoictif.androidgenericadapter.data.remote.response.NewBieResObject
import com.danhdueexoictif.androidgenericadapter.data.repository.SampleRepository
import com.danhdueexoictif.androidgenericadapter.utils.extension.getAccessToken
import kotlinx.coroutines.Deferred

class SampleRepositoryImpl(
    private val apiService: ApiService,
    private val sharedPref: SharedPreferenceHelper
) : SampleRepository {

    override fun getNewbies(
        page: Int
    ): Deferred<NetworkResponse<NewBieResObject, *>> =
        apiService.getNewbiesAsync(sharedPref.getAccessToken().getAccessToken())

    override fun createBrandMemberIdAsync(d3t3: String?): Deferred<NetworkResponse<MemberResObject, *>> =
        apiService.createBrandMemberIdAsync(
            MemberReqObject(
                sharedPref.getUUID(),
                BuildConfig.SITE_SERIAL,
                d3t3
            )
        )
}
