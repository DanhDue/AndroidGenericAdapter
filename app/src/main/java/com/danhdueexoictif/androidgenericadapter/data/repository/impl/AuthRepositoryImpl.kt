package com.danhdueexoictif.androidgenericadapter.data.repository.impl

import com.danhdueexoictif.androidgenericadapter.BuildConfig
import com.danhdueexoictif.androidgenericadapter.data.bean.LogoutResponse
import com.danhdueexoictif.androidgenericadapter.data.bean.MemberResObject
import com.danhdueexoictif.androidgenericadapter.data.bean.OauthToken
import com.danhdueexoictif.androidgenericadapter.data.local.pref.SharedPreferenceHelper
import com.danhdueexoictif.androidgenericadapter.data.remote.ApiService
import com.danhdueexoictif.androidgenericadapter.data.remote.NetworkResponse
import com.danhdueexoictif.androidgenericadapter.data.remote.request.MemberReqObject
import com.danhdueexoictif.androidgenericadapter.data.repository.AuthRepository
import com.danhdueexoictif.androidgenericadapter.utils.extension.getAccessToken
import kotlinx.coroutines.Deferred

class AuthRepositoryImpl(
    private val apiService: ApiService,
    private val sharedPref: SharedPreferenceHelper
) : AuthRepository {
    companion object {
        private const val GOOGLE = "google"
    }

    override fun loginWithGoogleAsync(accessToken: String): Deferred<NetworkResponse<OauthToken, *>> =
        apiService.login("", 1, "", GOOGLE, accessToken)

    override suspend fun logout(): NetworkResponse<LogoutResponse, *> {
        val accessToken =
            sharedPref.getAccessToken().getAccessToken()
                ?: return NetworkResponse.ServerError(null, 404, null)
        return apiService.logout(accessToken, sharedPref.getAccessToken())
    }

    override fun createBrandMemberIdAsync(
        d3t3: String?
    ): Deferred<NetworkResponse<MemberResObject, *>> =
        apiService.createBrandMemberIdAsync(MemberReqObject(sharedPref.getUUID(), BuildConfig.SITE_SERIAL, d3t3))
}
