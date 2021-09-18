package com.raantech.awfrlak.store.data.daos.remote.accessories

import com.raantech.awfrlak.store.data.api.response.ResponseWrapper
import com.raantech.awfrlak.store.data.common.NetworkConstants
import com.raantech.awfrlak.store.data.models.home.*
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface AccessoriesRemoteDao {

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("app/home")
    suspend fun getHome(
    ): ResponseWrapper<HomeResponse>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("mobiles")
    suspend fun getMobiles(
        @Query("skip") skip: Int,
        @Query("store_id") store_id: Int?,
        @Query("search_text") search_text: String?
    ): ResponseWrapper<List<MobilesItem>>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("accessories")
    suspend fun getAccessories(
        @Query("skip") skip: Int,
        @Query("store_id") store_id: Int?,
        @Query("search_text") search_text: String?
    ): ResponseWrapper<List<AccessoriesItem>>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("stores")
    suspend fun getStores(
        @Query("skip") skip: Int,
        @Query("search_text") search_text: String?
    ): ResponseWrapper<List<Store>>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("services")
    suspend fun getServices(
        @Query("skip") skip: Int,
        @Query("store_id") store_id: Int?,
        @Query("search_text") search_text: String?
    ): ResponseWrapper<List<Service>>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("accessories/{id}/show")
    suspend fun getAccessory(
        @Path("id") id: Int
    ): ResponseWrapper<AccessoriesItem>

}