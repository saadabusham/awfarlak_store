package com.raantech.awfrlak.store.ui.main.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.raantech.awfrlak.store.data.api.response.APIResource
import com.raantech.awfrlak.store.data.api.response.ResponseSubErrorsCodeEnum
import com.raantech.awfrlak.store.data.enums.UserEnums
import com.raantech.awfrlak.store.data.models.Price
import com.raantech.awfrlak.store.data.models.StoreStatistics
import com.raantech.awfrlak.store.data.models.home.AccessoriesItem
import com.raantech.awfrlak.store.data.models.home.MobilesItem
import com.raantech.awfrlak.store.data.models.home.Service
import com.raantech.awfrlak.store.data.models.home.Store
import com.raantech.awfrlak.store.data.pref.configuration.ConfigurationPref
import com.raantech.awfrlak.store.data.pref.user.UserPref
import com.raantech.awfrlak.store.data.repos.accessories.AccessoriesRepo
import com.raantech.awfrlak.store.data.repos.auth.UserRepo
import com.raantech.awfrlak.store.data.repos.configuration.ConfigurationRepo
import com.raantech.awfrlak.store.data.repos.wishlist.WishListRepo
import com.raantech.awfrlak.store.ui.base.viewmodel.BaseViewModel
import com.raantech.awfrlak.store.utils.pref.SharedPreferencesUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeneralViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val sharedPreferencesUtil: SharedPreferencesUtil,
    private val userPref: UserPref,
    private val configurationPref: ConfigurationPref,
    private val configurationRepo: ConfigurationRepo,
    private val wishListRepo: WishListRepo,
    private val accessoriesRepo: AccessoriesRepo,
) : BaseViewModel() {

    val addSelected = MutableLiveData(false)
    val isStatisticsEmpty = MutableLiveData(true)
    val storeStatistics: MutableLiveData<StoreStatistics> = MutableLiveData(StoreStatistics())

    fun logoutRemote() = liveData {
        emit(APIResource.loading())
        val response = userRepo.logout()
        emit(response)
    }

    fun logoutLocale() {
        if (userRepo.getTouchIdStatus())
            sharedPreferencesUtil.logout()
        else {
            sharedPreferencesUtil.clearPreference()
            userPref.setIsFirstOpen(false)
        }
    }

    fun getAccessories(
        skip: Int
    ) = liveData {
        emit(APIResource.loading())
        val response = accessoriesRepo.getAccessories(skip)
        emit(response)
    }

    fun getStores(
        skip: Int
    ) = liveData {
        emit(APIResource.loading())
        val response = accessoriesRepo.getStores(skip)
        emit(response)
    }

    fun getServices(
        skip: Int
    ) = liveData {
        emit(APIResource.loading())
        val response = accessoriesRepo.getServices(skip)
        emit(response)
    }

    fun getHome() = liveData {
        emit(APIResource.loading())
        val response = accessoriesRepo.getHome()
        emit(response)
    }

    fun getMobiles(
        skip: Int
    ) = liveData {
        emit(APIResource.loading())
        val response = accessoriesRepo.getMobiles(skip)
        emit(response)
    }

    fun isUserLoggedIn(): Boolean {
        return userRepo.getUserStatus() == UserEnums.UserState.LoggedIn
    }

    fun addToWishList(
        entityType: String,
        productId: Int
    ) = liveData {
        emit(APIResource.loading())
        val response =
            wishListRepo.addToWishList(entityType, productId)
        emit(response)
    }

    fun removeFromWishList(
        entity_type: String,
        productId: Int
    ) = liveData {
        emit(APIResource.loading())
        val response =
            wishListRepo.removeFromWishList(entity_type, productId)
        emit(response)
    }

    fun getStatistics(
    ) = liveData {
        emit(APIResource.loading())
        val response =
            userRepo.getStatistics()
        emit(response)
    }


}