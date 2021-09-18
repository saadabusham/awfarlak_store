package com.raantech.awfrlak.store.ui.more.aboutus

import androidx.lifecycle.liveData
import com.raantech.awfrlak.store.data.api.response.APIResource
import com.raantech.awfrlak.store.data.models.configuration.ConfigurationWrapperResponse
import com.raantech.awfrlak.store.data.pref.configuration.ConfigurationPref
import com.raantech.awfrlak.store.data.repos.configuration.ConfigurationRepo
import com.raantech.awfrlak.store.ui.base.viewmodel.BaseViewModel
import com.raantech.awfrlak.store.utils.pref.SharedPreferencesUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AboutUsViewModel @Inject constructor(
        val sharedPreferencesUtil: SharedPreferencesUtil,
        val configurationPref: ConfigurationPref,
        private val configurationRepo: ConfigurationRepo
) : BaseViewModel() {

    fun getAboutUs() = liveData {
        emit(APIResource.loading())
        val response = configurationRepo.getAboutUs()
        emit(response)
    }


    fun getSocialMedia(): ConfigurationWrapperResponse? {
        return sharedPreferencesUtil.getConfigurationPreferences()
    }

}