package com.raantech.awfrlak.store.ui.main.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.raantech.awfrlak.store.data.api.response.APIResource
import com.raantech.awfrlak.store.data.api.response.RequestStatusEnum
import com.raantech.awfrlak.store.data.api.response.ResponseSubErrorsCodeEnum
import com.raantech.awfrlak.store.data.enums.UserEnums
import com.raantech.awfrlak.store.data.models.Price
import com.raantech.awfrlak.store.data.models.home.AccessoriesItem
import com.raantech.awfrlak.store.data.models.home.MobilesItem
import com.raantech.awfrlak.store.data.models.home.Service
import com.raantech.awfrlak.store.data.models.home.Store
import com.raantech.awfrlak.store.data.pref.configuration.ConfigurationPref
import com.raantech.awfrlak.store.data.pref.user.UserPref
import com.raantech.awfrlak.store.data.repos.accessories.AccessoriesRepo
import com.raantech.awfrlak.store.data.repos.auth.UserRepo
import com.raantech.awfrlak.store.data.repos.cart.cart.CartRepo
import com.raantech.awfrlak.store.data.repos.cart.mobilecart.MobileCartRepo
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
        private val cartRepo: CartRepo,
        private val wishListRepo: WishListRepo,
        private val accessoriesRepo: AccessoriesRepo,
        private val mobileCartRepo: MobileCartRepo
) : BaseViewModel() {

    val addSelected = MutableLiveData(false)

    val cartCount: MutableLiveData<String> = MutableLiveData("0")
    var serviceToView: Service? = null
    var mobileToView: MobilesItem? = null
    var accessoryToView: AccessoriesItem? = null
    var storeToView: Store? = null

    val mobilesItemCount: MutableLiveData<Int> = MutableLiveData(1)
    val mobilesItemsPrice: MutableLiveData<Price> = MutableLiveData()

    val accessoriesItemCount: MutableLiveData<Int> = MutableLiveData(1)
    val accessoriesItemsPrice: MutableLiveData<Price> = MutableLiveData()

    fun getCartsCount() = viewModelScope.launch {
        cartRepo.getCartsCount().observeForever {
            viewModelScope.launch {
                val mobileCount = mobileCartRepo.getCartsCountInt()
                if (it != null)
                    cartCount.postValue(it.plus(mobileCount ?: 0).toString())
                else {
                    if (mobileCount == null)
                        cartCount.postValue("0")
                    else
                        cartCount.postValue(mobileCount.toString())
                }
            }
        }
        mobileCartRepo.getCartsCount().observeForever {
            viewModelScope.launch {
                val accessoriesCount = cartRepo.getCartsCountInt()
                if (it != null)
                    cartCount.postValue(it.plus(accessoriesCount ?: 0).toString())
                else {
                    if (accessoriesCount == null)
                        cartCount.postValue("0")
                    else
                        cartCount.postValue(accessoriesCount.toString())
                }
            }
        }
    }

    fun logoutRemote() = liveData {
        emit(APIResource.loading())
        val response = userRepo.logout()
        if (response.statusCode == ResponseSubErrorsCodeEnum.Success.value) {
            cartRepo.clearCart()
            mobileCartRepo.clearCart()
        }
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

    fun addToMobileCart(mobilesItem: MobilesItem) = viewModelScope.launch {
        mobileCartRepo.saveCart(mobilesItem)
    }

    fun getMobileCarts() = liveData {
        val response = mobileCartRepo.loadCarts()
        emit(response)
    }

    fun getMobileCart(id: Int) = liveData {
        val response = mobileCartRepo.getCart(id)
        emit(response)
    }

    fun plusMobiles() {
        mobilesItemCount.value = (mobilesItemCount.value?.plus(1))
        updateMobilesPrice()
    }

    fun minusMobiles() {
        if (mobilesItemCount.value == 1)
            return
        mobilesItemCount.value = (mobilesItemCount.value?.minus(1))
        updateMobilesPrice()
    }

    fun updateMobilesPrice() {
        mobilesItemsPrice.value = (Price(formatted = "${mobilesItemCount.value?.times(mobileToView?.price?.amount?.toDoubleOrNull() ?: 0.0)}${mobileToView?.price?.currency}"))
    }

    fun onAddMobileToCartClicked() {
        mobileToView?.count = mobilesItemCount.value
        mobileToView?.let { addToMobileCart(it) }
    }

    fun addToAccesoriesCart(accessory: AccessoriesItem) = viewModelScope.launch {
        cartRepo.saveCart(accessory)
    }

    fun getAccessoriesCarts() = liveData {
        val response = cartRepo.loadCarts()
        emit(response)
    }

    fun plusAccessories() {
        accessoriesItemCount.value = (accessoriesItemCount.value?.plus(1))
        updateAccessoriesPrice()
    }

    fun minusAccessories() {
        if (accessoriesItemCount.value == 1)
            return
        accessoriesItemCount.value = (accessoriesItemCount.value?.minus(1))
        updateAccessoriesPrice()
    }

    fun updateAccessoriesPrice() {
        accessoriesItemsPrice.value = (Price(formatted = "${accessoriesItemCount.value?.times(accessoryToView?.price?.amount?.toDoubleOrNull() ?: 0.0)}${accessoryToView?.price?.currency}"))
    }

    fun onAddAccessoriesToCartClicked() {
        accessoryToView?.count = accessoriesItemCount.value
        accessoryToView?.let { addToAccesoriesCart(it) }
    }

    fun getAccessoryCart(id: Int) = liveData {
        val response = cartRepo.getCart(id)
        emit(response)
    }

}