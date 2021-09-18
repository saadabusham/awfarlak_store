package com.raantech.awfrlak.store.data.daos.remote.user

import com.raantech.awfrlak.store.data.api.response.ResponseWrapper
import com.raantech.awfrlak.store.data.common.NetworkConstants
import com.raantech.awfrlak.store.data.models.auth.login.TokenModel
import com.raantech.awfrlak.store.data.models.auth.login.UserDetailsResponseModel
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserRemoteDao {

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:true")
    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("phone_number") phoneNumber: String
    ): ResponseWrapper<TokenModel>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @POST("auth/logout")
    suspend fun logout(
    ): ResponseWrapper<Any>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:true")
    @FormUrlEncoded
    @POST("auth/resend")
    suspend fun resendCode(
        @Field("token") token: String
    ): ResponseWrapper<TokenModel>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:true")
    @FormUrlEncoded
    @POST("auth/verify")
    suspend fun verify(
        @Field("token") token: String,
        @Field("code") code: Int,
        @Field("device_token") device_token: String,
        @Field("platform") platform: String
    ): ResponseWrapper<UserDetailsResponseModel>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:true")
    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("token") token: String,
        @Field("name") name: String,
        @Field("address") address: String,
        @Field("email") email: String
    ): ResponseWrapper<UserDetailsResponseModel>

}