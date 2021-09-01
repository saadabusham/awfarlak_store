package com.raantech.awfrlak.ui.main.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.raantech.awfrlak.common.CommonEnums
import com.raantech.awfrlak.data.api.response.APIResource
import com.raantech.awfrlak.data.enums.ServicesType
import com.raantech.awfrlak.data.enums.UserEnums
import com.raantech.awfrlak.data.models.Price
import com.raantech.awfrlak.data.models.accessories.Accessory
import com.raantech.awfrlak.data.models.home.MobilesItem
import com.raantech.awfrlak.data.models.home.Service
import com.raantech.awfrlak.data.pref.configuration.ConfigurationPref
import com.raantech.awfrlak.data.pref.user.UserPref
import com.raantech.awfrlak.data.repos.accessories.AccessoriesRepo
import com.raantech.awfrlak.data.repos.auth.UserRepo
import com.raantech.awfrlak.data.repos.cart.cart.CartRepo
import com.raantech.awfrlak.data.repos.cart.mobilecart.MobileCartRepo
import com.raantech.awfrlak.data.repos.configuration.ConfigurationRepo
import com.raantech.awfrlak.data.repos.wishlist.WishListRepo
import com.raantech.awfrlak.ui.base.viewmodel.BaseViewModel
import com.raantech.awfrlak.utils.pref.SharedPreferencesUtil
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

    val cartCount: MutableLiveData<String> = MutableLiveData("0")
    var serviceToView: Service? = null
    var mobileToView: MobilesItem? = null

    val itemCount: MutableLiveData<Int> = MutableLiveData(1)
    val itemsPrice: MutableLiveData<Price> = MutableLiveData()

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
            skip: Int,
            storeId: Int? = null
    ) = liveData {
        emit(APIResource.loading())
        val response = accessoriesRepo.getAccessories(skip, storeId)
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
            skip: Int,
            storeId: Int? = null
    ) = liveData {
        emit(APIResource.loading())
        val response = accessoriesRepo.getServices(skip, storeId)
        emit(response)
    }

    fun getHome() = liveData {
        emit(APIResource.loading())
        val response = accessoriesRepo.getHome()
        emit(response)
    }

    fun getMobiles(
            skip: Int,
            storeId: Int? = null
    ) = liveData {
        emit(APIResource.loading())
        val response = accessoriesRepo.getMobiles(skip, storeId)
        emit(response)
    }

    fun isUserLoggedIn(): Boolean {
        return userRepo.getUserStatus() == UserEnums.UserState.LoggedIn
    }

    fun saveLanguage() = liveData {
        configurationPref.setAppLanguageValue(if (configurationPref.getAppLanguageValue() == "ar") CommonEnums.Languages.English.value else CommonEnums.Languages.Arabic.value)
        emit(null)
    }

    fun getAppLanguage(): String {
        return configurationPref.getAppLanguageValue()
    }

    fun getCities() = liveData {
        emit(APIResource.loading())
        val response = configurationRepo.getCities()
        emit(response)
    }

    fun addToWishList(
            productId: Int
    ) = liveData {
        emit(APIResource.loading())
        val response =
                wishListRepo.addToWishList(ServicesType.PRODUCT.value, productId)
        emit(response)
    }

    fun removeFromWishList(
            productId: Int
    ) = liveData {
        emit(APIResource.loading())
        val response =
                wishListRepo.removeFromWishList(productId)
        emit(response)
    }

    fun addToCart(accessory: Accessory) = viewModelScope.launch {
        cartRepo.saveCart(accessory)
    }

    fun getCarts() = liveData {
        val response = cartRepo.loadCarts()
        emit(response)
    }

    fun getCart(id: Int) = liveData {
        val response = cartRepo.getCart(id)
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

    fun plus() {
        itemCount.value = (itemCount.value?.plus(1))
        updatePrice()
    }

    fun minus() {
        if (itemCount.value == 1)
            return
        itemCount.value = (itemCount.value?.minus(1))
        updatePrice()
    }

    fun updatePrice() {
        itemsPrice.value = (Price(formatted = "${itemCount.value?.times(mobileToView?.price?.amount?.toDoubleOrNull() ?: 0.0)}${mobileToView?.price?.currency}"))

    }

    fun onAddToCartClicked() {
        mobileToView?.count = itemCount.value
        mobileToView?.let { addToMobileCart(it) }
    }

}