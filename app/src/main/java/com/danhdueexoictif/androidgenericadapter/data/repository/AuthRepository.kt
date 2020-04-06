package com.danhdueexoictif.androidgenericadapter.data.repository

import com.danhdueexoictif.androidgenericadapter.data.bean.LogoutResponse
import com.danhdueexoictif.androidgenericadapter.data.bean.MemberResObject
import com.danhdueexoictif.androidgenericadapter.data.bean.OauthToken
import com.danhdueexoictif.androidgenericadapter.data.remote.NetworkResponse
import kotlinx.coroutines.Deferred

interface AuthRepository {
    fun loginWithGoogleAsync(accessToken: String): Deferred<NetworkResponse<OauthToken, *>>

    /**
     * create Brand Member ID
     * @param kokokuId is nullable.
     * @return Deferred is a job with NetworkResponse result.
     */
    fun createBrandMemberIdAsync(kokokuId: String?): Deferred<NetworkResponse<MemberResObject, *>>
    suspend fun logout(): NetworkResponse<LogoutResponse, *>
}
