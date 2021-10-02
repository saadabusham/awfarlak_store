package com.raantech.awfrlak.store.ui.accessory.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.raantech.awfrlak.store.data.api.response.APIResource
import com.raantech.awfrlak.store.data.models.*
import com.raantech.awfrlak.store.data.models.home.AccessoriesItem
import com.raantech.awfrlak.store.data.models.media.Media
import com.raantech.awfrlak.store.data.repos.accessories.AccessoriesRepo
import com.raantech.awfrlak.store.data.repos.configuration.ConfigurationRepo
import com.raantech.awfrlak.store.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccessoryViewModel @Inject constructor(
        private val configurationRepo: ConfigurationRepo,
        private val accessoriesRepo: AccessoriesRepo,
) : BaseViewModel() {

    var accessoryToView: AccessoriesItem? = null
    val name: MutableLiveData<String> = MutableLiveData()
    val price: MutableLiveData<String> = MutableLiveData()
    val mobileInfo: MutableLiveData<String> = MutableLiveData()
    val isAvailable: MutableLiveData<Boolean> = MutableLiveData(true)

    fun getAccessoriesType(
    ) = liveData {
        emit(APIResource.loading())
        val response = configurationRepo.getAccessoryTypes()
        emit(response)
    }

    fun getAccessoryDedicated(
    ) = liveData {
        emit(APIResource.loading())
        val response = configurationRepo.getAccessoryDedicated()
        emit(response)
    }


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
        val response = accessoriesRepo.updateAccessory(accessoryToView?.id ?: 0, accessoryRequest)
        emit(response)
    }

    fun deleteAccessory(
    ) = liveData {
        emit(APIResource.loading())
        val response = accessoriesRepo.deleteAccessory(accessoryToView?.id ?: 0)
        emit(response)
    }


    fun buildAccessory(): AccessoryRequest {
        return AccessoryRequest(
                isInStock = true,
                isActive = accessoryToView?.isActive,
                price = accessoryToView?.price?.amount?.toDoubleOrNull(),
                name = accessoryToView?.name,
                description = accessoryToView?.description,
                files = Files(
                        baseImage = accessoryToView?.baseImage?.id,
                        additionalImages = accessoryToView?.additionalImages?.map { it.id ?: 0 }
                ),
                accessoryDedicatedId = accessoryToView?.accessoryDedicated?.id,
                accessoryTypeId = accessoryToView?.accessoryType?.id
        )
    }

    fun buildAccessoryItem(
            additionalImages: List<Media>,
            type: GeneralLookup,
            dedicatedFor: GeneralLookup,
            baseImage: Media
    ): AccessoriesItem {
        return AccessoriesItem(
                id = accessoryToView?.id,
                additionalImages = additionalImages,
                isActive = isAvailable.value,
                description = mobileInfo.value,
                baseImage = baseImage,
                price = Price(amount = price.value),
                name = name.value,
                accessoryType = type,
                accessoryDedicated = dedicatedFor
        )
    }

    fun fillData() {
        accessoryToView?.let {
            name.postValue(it.name)
            mobileInfo.postValue(it.description)
            price.postValue(it.price?.amount)
            isAvailable.postValue(it.isActive)
        }
    }
}