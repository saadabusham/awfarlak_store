package com.raantech.awfrlak.store.ui.service.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.raantech.awfrlak.store.data.api.response.APIResource
import com.raantech.awfrlak.store.data.models.*
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

    fun addService(
            serviceRequest: ServiceRequest
    ) = liveData {
        emit(APIResource.loading())
        val response = accessoriesRepo.addService(serviceRequest)
        emit(response)
    }

    fun updateService(
            serviceRequest: ServiceRequest
    ) = liveData {
        emit(APIResource.loading())
        val response = accessoriesRepo.updateService(serviceToView?.id ?: 0, serviceRequest)
        emit(response)
    }

    fun deleteService(
    ) = liveData {
        emit(APIResource.loading())
        val response = accessoriesRepo.deleteService(serviceToView?.id ?: 0)
        emit(response)
    }


    fun buildService(): ServiceRequest {
        return ServiceRequest(
                isActive = true,
                isAvailable = serviceToView?.isActive,
                hasDelivery = serviceToView?.hasDelivery,
                price = serviceToView?.price?.amount?.toDoubleOrNull(),
                name = serviceToView?.name,
                serviceCompletionTime = serviceToView?.serviceCompletionTime,
                description = serviceToView?.description,
                files = Files(
                        baseImage = serviceToView?.base_image?.id,
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
                base_image = baseImage,
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