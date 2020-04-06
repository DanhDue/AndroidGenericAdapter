package com.danhdueexoictif.androidgenericadapter.data.repository

import com.danhdueexoictif.androidgenericadapter.data.bean.MemberResObject
import com.danhdueexoictif.androidgenericadapter.data.remote.NetworkResponse
import com.danhdueexoictif.androidgenericadapter.data.remote.response.NewBieResObject
import kotlinx.coroutines.Deferred

interface SampleRepository {
    fun getNewbies(page: Int): Deferred<NetworkResponse<NewBieResObject, *>>

    /**
     * create Brand Member ID
     * @param kokokuId is nullable.
     * @return Deferred is a job with NetworkResponse result.
     */
    fun createBrandMemberIdAsync(kokokuId: String?): Deferred<NetworkResponse<MemberResObject, *>>
}
