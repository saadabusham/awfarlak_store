package com.technzone.phoneapp.data.daos.remote.configuration

import com.technzone.phoneapp.data.api.response.ResponseWrapper
import com.technzone.phoneapp.data.common.NetworkConstants
import com.technzone.phoneapp.data.models.City
import com.technzone.phoneapp.data.models.configuration.ConfigurationWrapperResponse
import com.technzone.phoneapp.data.models.more.AboutUsResponse
import retrofit2.http.GET
import retrofit2.http.Headers

interface ConfigurationRemoteDao {

    @GET("app/settings")
    suspend fun getAppConfiguration(): ResponseWrapper<ConfigurationWrapperResponse>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("app/cities")
    suspend fun getCities(
    ): ResponseWrapper<List<City>>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:true")
    @GET("app/aboutUs")
    suspend fun getAboutUs(): ResponseWrapper<AboutUsResponse>


}