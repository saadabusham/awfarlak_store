package com.raantech.awfrlak.store.data.daos.remote.accessories

import com.raantech.awfrlak.store.data.api.response.ResponseWrapper
import com.raantech.awfrlak.store.data.common.NetworkConstants
import com.raantech.awfrlak.store.data.models.AccessoryRequest
import com.raantech.awfrlak.store.data.models.MobileRequest
import com.raantech.awfrlak.store.data.models.home.*
import retrofit2.http.*

interface AccessoriesRemoteDao {

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("app/home")
    suspend fun getHome(
    ): ResponseWrapper<HomeResponse>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("merchant/mobiles")
    suspend fun getMobiles(
        @Query("skip") skip: Int
    ): ResponseWrapper<List<MobilesItem>>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("merchant/accessories")
    suspend fun getAccessories(
        @Query("skip") skip: Int
    ): ResponseWrapper<List<AccessoriesItem>>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("stores")
    suspend fun getStores(
        @Query("skip") skip: Int
    ): ResponseWrapper<List<Store>>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("merchant/services")
    suspend fun getServices(
        @Query("skip") skip: Int
    ): ResponseWrapper<List<Service>>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("merchant/accessories/{id}/show")
    suspend fun getAccessory(
        @Path("id") id: Int
    ): ResponseWrapper<AccessoriesItem>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @POST("merchant/mobiles/store")
    suspend fun addMobile(
        @Body mobileRequest: MobileRequest
    ): ResponseWrapper<MobilesItem>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @PUT("merchant/mobiles/{id}/update")
    suspend fun updateMobile(
        @Path("id") id: Int,
        @Body mobileRequest: MobileRequest
    ): ResponseWrapper<MobilesItem>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @PUT("merchant/mobiles/{id}/destroy")
    suspend fun deleteMobile(
        @Path("id") id: Int
    ): ResponseWrapper<MobilesItem>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @POST("merchant/accessories/store")
    suspend fun addAccessories(
        @Body accessoryRequest: AccessoryRequest
    ): ResponseWrapper<AccessoriesItem>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @PUT("merchant/accessories/{id}/update")
    suspend fun updateAccessories(
        @Path("id") id: Int,
        @Body accessoryRequest: AccessoryRequest
    ): ResponseWrapper<AccessoriesItem>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @PUT("merchant/accessories/{id}/destroy")
    suspend fun deleteAccessories(
        @Path("id") id: Int
    ): ResponseWrapper<AccessoriesItem>

}