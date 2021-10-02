package com.raantech.awfrlak.store.data.repos.configuration

import com.raantech.awfrlak.store.common.CommonEnums
import com.raantech.awfrlak.store.data.api.response.APIResource
import com.raantech.awfrlak.store.data.api.response.ResponseHandler
import com.raantech.awfrlak.store.data.api.response.ResponseWrapper
import com.raantech.awfrlak.store.data.daos.remote.configuration.ConfigurationRemoteDao
import com.raantech.awfrlak.store.data.models.City
import com.raantech.awfrlak.store.data.models.GeneralLookup
import com.raantech.awfrlak.store.data.models.configuration.ConfigurationWrapperResponse
import com.raantech.awfrlak.store.data.models.more.AboutUsResponse
import com.raantech.awfrlak.store.data.pref.configuration.ConfigurationPref
import com.raantech.awfrlak.store.data.repos.base.BaseRepo
import javax.inject.Inject

class ConfigurationRepoImp @Inject constructor(
        responseHandler: ResponseHandler,
        private val configurationRemoteDao: ConfigurationRemoteDao,
        private val configurationPref: ConfigurationPref
) : BaseRepo(responseHandler), ConfigurationRepo {

    override fun setAppLanguage(selectedLanguage: CommonEnums.Languages) {
        configurationPref.setAppLanguageValue(selectedLanguage.value)
    }

    override fun getAppLanguage(): CommonEnums.Languages {
        return CommonEnums.Languages.getLanguageByValue(configurationPref.getAppLanguageValue())
    }

    override suspend fun loadConfigurationData():
            APIResource<ResponseWrapper<ConfigurationWrapperResponse>> {
        return try {
            responseHandle.handleSuccess(configurationRemoteDao.getAppConfiguration())
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun getCities(): APIResource<ResponseWrapper<List<City>>> {
        return try {
            responseHandle.handleSuccess(configurationRemoteDao.getCities())
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun getAboutUs(): APIResource<ResponseWrapper<AboutUsResponse>> {
        return try {
            responseHandle.handleSuccess(configurationRemoteDao.getAboutUs())
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun getMobileType(): APIResource<ResponseWrapper<List<GeneralLookup>>> {
        return try {
            responseHandle.handleSuccess(configurationRemoteDao.getMobileType())
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun getColor(): APIResource<ResponseWrapper<List<GeneralLookup>>> {
        return try {
            responseHandle.handleSuccess(configurationRemoteDao.getColor())
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun getStorage(): APIResource<ResponseWrapper<List<GeneralLookup>>> {
        return try {
            responseHandle.handleSuccess(configurationRemoteDao.getStorage())
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun getAccessoryDedicated(): APIResource<ResponseWrapper<List<GeneralLookup>>> {
        return try {
            responseHandle.handleSuccess(configurationRemoteDao.getAccessoryDedicated())
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun getAccessoryTypes(): APIResource<ResponseWrapper<List<GeneralLookup>>> {
        return try {
            responseHandle.handleSuccess(configurationRemoteDao.getAccessoryTypes())
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

}