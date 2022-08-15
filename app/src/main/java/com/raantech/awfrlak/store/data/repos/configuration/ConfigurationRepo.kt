package com.raantech.awfrlak.store.data.repos.configuration

import com.raantech.awfrlak.store.common.CommonEnums
import com.raantech.awfrlak.store.data.api.response.APIResource
import com.raantech.awfrlak.store.data.api.response.ResponseWrapper
import com.raantech.awfrlak.store.data.common.NetworkConstants
import com.raantech.awfrlak.store.data.models.CitiesResponse
import com.raantech.awfrlak.store.data.models.City
import com.raantech.awfrlak.store.data.models.GeneralLookup
import com.raantech.awfrlak.store.data.models.configuration.ConfigurationWrapperResponse
import com.raantech.awfrlak.store.data.models.more.AboutUsResponse
import retrofit2.http.GET
import retrofit2.http.Headers

interface ConfigurationRepo {

    fun setAppLanguage(selectedLanguage: CommonEnums.Languages)
    fun getAppLanguage(): CommonEnums.Languages

    suspend fun loadConfigurationData(): APIResource<ResponseWrapper<ConfigurationWrapperResponse>>

    suspend fun getCities(
    ): APIResource<ResponseWrapper<CitiesResponse>>

    suspend fun getAboutUs(): APIResource<ResponseWrapper<AboutUsResponse>>

    suspend fun getMobileType(): APIResource<ResponseWrapper<List<GeneralLookup>>>

    suspend fun getColor(): APIResource<ResponseWrapper<List<GeneralLookup>>>

    suspend fun getStorage(): APIResource<ResponseWrapper<List<GeneralLookup>>>

    suspend fun getAccessoryDedicated():
            APIResource<ResponseWrapper<List<GeneralLookup>>>

    suspend fun getAccessoryTypes():
            APIResource<ResponseWrapper<List<GeneralLookup>>>
}