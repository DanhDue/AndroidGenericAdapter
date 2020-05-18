package com.danhdueexoictif.androidgenericadapter.data.remote

import com.danhdueexoictif.androidgenericadapter.data.bean.LogoutResponse
import com.danhdueexoictif.androidgenericadapter.data.bean.MemberResObject
import com.danhdueexoictif.androidgenericadapter.data.bean.OauthToken
import com.danhdueexoictif.androidgenericadapter.data.remote.request.MemberReqObject
import com.danhdueexoictif.androidgenericadapter.data.remote.response.ErrorResponse
import com.danhdueexoictif.androidgenericadapter.data.remote.response.NewBieResObject
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("oauth/token")
    @FormUrlEncoded
    fun login(
        @Field("grant_type") grantType: String,
        @Field("client_id") clientId: Int,
        @Field("client_secret") clientSecret: String,
        @Field("service") provider: String,
        @Field("token") accessToken: String
    ): Deferred<NetworkResponse<OauthToken, ErrorResponse>>

    @POST("oauth/token")
    @FormUrlEncoded
    fun refreshToken(
        @Field("grant_type") grantType: String,
        @Field("client_id") clientId: Int,
        @Field("client_secret") clientSecret: String,
        @Field("refresh_token") refreshToken: String
    ): Call<OauthToken>

    @POST("logout")
    @FormUrlEncoded
    suspend fun logout(
        @Header("Authorization") token: String,
        @Field("fcmToken") fcmToken: String?
    ): NetworkResponse<LogoutResponse, ErrorResponse>

    @GET("SWFR01011991.seam")
    fun getNewbiesAsync(
        @Header("Authorization") token: String? = null
    ): Deferred<NetworkResponse<NewBieResObject, ErrorResponse>>

    /**
     * create a new brand member ID
     */
    @POST("register")
    fun createBrandMemberIdAsync(@Body memberReqObject: MemberReqObject): Deferred<NetworkResponse<MemberResObject, ErrorResponse>>
}
