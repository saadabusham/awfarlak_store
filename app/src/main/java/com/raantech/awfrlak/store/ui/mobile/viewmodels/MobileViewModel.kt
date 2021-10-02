package com.raantech.awfrlak.store.ui.mobile.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.raantech.awfrlak.store.data.api.response.APIResource
import com.raantech.awfrlak.store.data.models.Files
import com.raantech.awfrlak.store.data.models.GeneralLookup
import com.raantech.awfrlak.store.data.models.MobileRequest
import com.raantech.awfrlak.store.data.models.Price
import com.raantech.awfrlak.store.data.models.home.MobilesItem
import com.raantech.awfrlak.store.data.models.media.Media
import com.raantech.awfrlak.store.data.repos.accessories.AccessoriesRepo
import com.raantech.awfrlak.store.data.repos.configuration.ConfigurationRepo
import com.raantech.awfrlak.store.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MobileViewModel @Inject constructor(
    private val configurationRepo: ConfigurationRepo,
    private val accessoriesRepo: AccessoriesRepo,
) : BaseViewModel() {

    var mobileToView: MobilesItem? = null
    val name: MutableLiveData<String> = MutableLiveData()
    val price: MutableLiveData<String> = MutableLiveData()
    val mobileInfo: MutableLiveData<String> = MutableLiveData()
    val isAvailable: MutableLiveData<Boolean> = MutableLiveData(true)

    fun getMobileType(
    ) = liveData {
        emit(APIResource.loading())
        val response = configurationRepo.getMobileType()
        emit(response)
    }

    fun getColors(
    ) = liveData {
        emit(APIResource.loading())
        val response = configurationRepo.getColor()
        emit(response)
    }

    fun getStorage(
    ) = liveData {
        emit(APIResource.loading())
        val response = configurationRepo.getStorage()
        emit(response)
    }

    fun addMobile(
        mobileRequest: MobileRequest
    ) = liveData {
        emit(APIResource.loading())
        val response = accessoriesRepo.addMobile(mobileRequest)
        emit(response)
    }

    fun updateMobile(
        mobileRequest: MobileRequest
    ) = liveData {
        emit(APIResource.loading())
        val response = accessoriesRepo.updateMobile(mobileToView?.id ?: 0, mobileRequest)
        emit(response)
    }

    fun deleteMobile(
    ) = liveData {
        emit(APIResource.loading())
        val response = accessoriesRepo.deleteMobile(mobileToView?.id ?: 0)
        emit(response)
    }

    fun buildMobile(): MobileRequest {
        return MobileRequest(
            isInStock = true,
            isActive = mobileToView?.isActive,
            colorId = mobileToView?.color?.id,
            isNew = mobileToView?.isNew,
            price = mobileToView?.price?.amount?.toDoubleOrNull(),
            typeId = mobileToView?.type?.id,
            storageId = mobileToView?.storage?.id,
            name = mobileToView?.name,
            description = mobileToView?.description,
            simCardsNumbers = mobileToView?.simCardsNumbers,
            files = Files(
                baseImage = mobileToView?.baseImage?.id,
                additionalImages = mobileToView?.additionalImages?.map { it.id ?: 0 }
            )
        )
    }

    fun buildMobileItem(
        additionalImages: List<Media>,
        color: GeneralLookup,
        isNew: Boolean,
        simCardsNumbers: Int,
        storage: GeneralLookup,
        type: GeneralLookup,
        baseImage: Media
    ): MobilesItem {
        return MobilesItem(
            id = mobileToView?.id,
            additionalImages = additionalImages,
            isActive = isAvailable.value,
            color = color,
            isNew = isNew,
            simCardsNumbers = simCardsNumbers,
            description = mobileInfo.value,
            storage = storage,
            type = type,
            baseImage = baseImage,
            price = Price(amount = price.value),
            name = name.value
        )
    }

    fun fillData() {
        mobileToView?.let {
            name.postValue(it.name)
            mobileInfo.postValue(it.description)
            price.postValue(it.price?.amount)
            isAvailable.postValue(it.isActive)
        }
    }
}