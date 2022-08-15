package com.raantech.awfrlak.store.ui.citypicker.viewmodels

import androidx.lifecycle.liveData
import com.raantech.awfrlak.store.data.api.response.APIResource
import com.raantech.awfrlak.store.data.repos.configuration.ConfigurationRepo
import com.raantech.awfrlak.store.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CitiesViewModel @Inject constructor(
    private val configurationRepo : ConfigurationRepo
) : BaseViewModel() {

    fun getCities(
    ) = liveData {
        emit(APIResource.loading())
        val response = configurationRepo.getCities()
        emit(response)
    }

}