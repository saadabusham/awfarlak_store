package com.raantech.awfrlak.store.data.repos.configuration

import com.raantech.awfrlak.store.common.CommonEnums
import com.raantech.awfrlak.store.data.api.response.APIResource
import com.raantech.awfrlak.store.data.api.response.ResponseWrapper
import com.raantech.awfrlak.store.data.models.City
import com.raantech.awfrlak.store.data.models.configuration.ConfigurationWrapperResponse
import com.raantech.awfrlak.store.data.models.more.AboutUsResponse

interface ConfigurationRepo {

    fun setAppLanguage(selectedLanguage: CommonEnums.Languages)
    fun getAppLanguage(): CommonEnums.Languages

    suspend fun loadConfigurationData(): APIResource<ResponseWrapper<ConfigurationWrapperResponse>>

    suspend fun getCities(
    ): APIResource<ResponseWrapper<List<City>>>

    suspend fun getAboutUs(): APIResource<ResponseWrapper<AboutUsResponse>>
}