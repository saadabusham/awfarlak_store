package com.raantech.awfrlak.store.data.daos.remote.user

import com.raantech.awfrlak.store.data.api.response.ResponseWrapper
import com.raantech.awfrlak.store.data.common.NetworkConstants
import com.raantech.awfrlak.store.data.models.StoreStatistics
import com.raantech.awfrlak.store.data.models.auth.login.TokenModel
import com.raantech.awfrlak.store.data.models.auth.login2.UserDetailsResponseModel
import com.raantech.awfrlak.store.data.models.auth.login2.UserInfo
import com.raantech.awfrlak.store.data.models.notification.Notification
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface UserRemoteDao {

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:true")
    @FormUrlEncoded
    @POST("merchant/auth/login")
    suspend fun login(
        @Field("phone_number") phoneNumber: String
    ): ResponseWrapper<TokenModel>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @POST("merchant/auth/logout")
    suspend fun logout(
    ): ResponseWrapper<Any>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:true")
    @FormUrlEncoded
    @POST("merchant/auth/resend")
    suspend fun resendCode(
        @Field("token") token: String
    ): ResponseWrapper<TokenModel>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:true")
    @FormUrlEncoded
    @POST("merchant/auth/verify")
    suspend fun verify(
        @Field("token") token: String,
        @Field("code") code: Int,
        @Field("device_token") device_token: String,
        @Field("platform") platform: String
    ): ResponseWrapper<UserDetailsResponseModel>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:true")
    @Multipart
    @POST("merchant/auth/register")
    suspend fun register(
        @Part("store_name") storeName: RequestBody,
        @Part("city") city: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part commercialRegister: MultipartBody.Part,
        @Part("responsible_person") responsible_person: RequestBody,
        @Part logo: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("phone_number") phone_number: RequestBody,
        @Part additionalImages: List<MultipartBody.Part>
    ): ResponseWrapper<UserDetailsResponseModel>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @POST("merchant/profile/update")
    suspend fun updateProfile(
        @Query("name") name: String,
        @Query("email") email: String
    ): ResponseWrapper<UserInfo>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @POST("merchant/profile/update/address")
    suspend fun updateAddress(
        @Query("name") name: String,
        @Query("phone") phone: String,
        @Query("city") city: String,
        @Query("district") district: String,
        @Query("street") street: String,
        @Query("building_number") building_number: String,
        @Query("description") description: String,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
    ): ResponseWrapper<UserInfo>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("merchant/notifications")
    suspend fun getNotifications(
        @Query("skip") skip: Int
    ): ResponseWrapper<List<Notification>>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("merchant/statistics")
    suspend fun getStatistics(
    ): ResponseWrapper<StoreStatistics>

}