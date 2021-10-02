package com.raantech.awfrlak.store.ui.service.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.raantech.awfrlak.store.data.api.response.APIResource
import com.raantech.awfrlak.store.data.models.*
import com.raantech.awfrlak.store.data.models.home.AccessoriesItem
import com.raantech.awfrlak.store.data.models.home.Service
import com.raantech.awfrlak.store.data.models.media.Media
import com.raantech.awfrlak.store.data.repos.accessories.AccessoriesRepo
import com.raantech.awfrlak.store.data.repos.configuration.ConfigurationRepo
import com.raantech.awfrlak.store.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ServiceViewModel @Inject constructor(
        private val configurationRepo: ConfigurationRepo,
        private val accessoriesRepo: AccessoriesRepo,
) : BaseViewModel() {

    var serviceToView: Service? = null
    val name: MutableLiveData<String> = MutableLiveData()
    val price: MutableLiveData<String> = MutableLiveData()
    val time: MutableLiveData<String> = MutableLiveData()
    val mobileInfo: MutableLiveData<String> = MutableLiveData()
    val isAvailable: MutableLiveData<Boolean> = MutableLiveData(true)
    val deliveryAvailable: MutableLiveData<Boolean> = MutableLiveData(true)

    fun addAccessory(
            accessoryRequest: AccessoryRequest
    ) = liveData {
        emit(APIResource.loading())
        val response = accessoriesRepo.addAccessory(accessoryRequest)
        emit(response)
    }

    fun updateAccessory(
            accessoryRequest: AccessoryRequest
    ) = liveData {
        emit(APIResource.loading())
        val response = accessoriesRepo.updateAccessory(serviceToView?.id ?: 0, accessoryRequest)
        emit(response)
    }

    fun deleteAccessory(
    ) = liveData {
        emit(APIResource.loading())
        val response = accessoriesRepo.deleteAccessory(serviceToView?.id ?: 0)
        emit(response)
    }


    fun buildAccessory(): ServiceRequest {
        return ServiceRequest(
                isActive = serviceToView?.isActive,
                hasDelivery = serviceToView?.hasDelivery,
                price = serviceToView?.price?.amount?.toDoubleOrNull(),
                name = serviceToView?.name,
                serviceCompletionTime = time.value,
                description = serviceToView?.description,
                files = Files(
                        baseImage = serviceToView?.logo?.id,
                        additionalImages = serviceToView?.additionalImages?.map { it.id ?: 0 }
                )
        )
    }

    fun buildService(
            additionalImages: List<Media>,
            baseImage: Media
    ): Service {
        return Service(
                id = serviceToView?.id,
                additionalImages = additionalImages,
                isActive = isAvailable.value,
                description = mobileInfo.value,
                logo = baseImage,
                price = Price(amount = price.value),
                name = name.value,
                hasDelivery = deliveryAvailable.value,
                serviceCompletionTime = time.value
        )
    }

    fun fillData() {
        serviceToView?.let {
            name.postValue(it.name)
            mobileInfo.postValue(it.description)
            price.postValue(it.price?.amount)
            isAvailable.postValue(it.isActive)
            deliveryAvailable.postValue(it.hasDelivery)
            time.postValue(it.serviceCompletionTime)
        }
    }
}