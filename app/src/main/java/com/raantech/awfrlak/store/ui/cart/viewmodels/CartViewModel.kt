package com.raantech.awfrlak.store.ui.cart.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.raantech.awfrlak.store.data.models.Price
import com.raantech.awfrlak.store.data.models.home.AccessoriesItem
import com.raantech.awfrlak.store.data.models.home.MobilesItem
import com.raantech.awfrlak.store.data.repos.cart.cart.CartRepo
import com.raantech.awfrlak.store.data.repos.cart.mobilecart.MobileCartRepo
import com.raantech.awfrlak.store.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
        private val cartRepo: CartRepo,
        private val mobileCartRepo: MobileCartRepo
) : BaseViewModel() {
    companion object {
    }

    val cartCount: MutableLiveData<String> = MutableLiveData("0")
    val TAX_CONST: Double = 0.15
    var tax: MutableLiveData<Price> = MutableLiveData()
    var subTotal: MutableLiveData<Price> = MutableLiveData()
    var total: MutableLiveData<Price> = MutableLiveData()

    fun updateAccessoryCartItem(accessory: AccessoriesItem) = viewModelScope.launch {
        cartRepo.saveCart(accessory)
    }

    fun deleteAccessoryCart(id: Int) = viewModelScope.launch {
        cartRepo.deleteCart(id)
    }
    fun updateMobileCartItem(mobilesItem: MobilesItem) = viewModelScope.launch {
        mobileCartRepo.saveCart(mobilesItem)
    }

    fun deleteMobileCart(id: Int) = viewModelScope.launch {
        mobileCartRepo.deleteCart(id)
    }

    fun getCarts() = liveData {
        val list = mutableListOf<Any>()
        val accessoryCart = cartRepo.loadCarts()
        val mobilesCart = mobileCartRepo.loadCarts()
        list.addAll(accessoryCart)
        list.addAll(mobilesCart)
        emit(list)
    }

    fun getCart(id: Int) = liveData {
        val response = cartRepo.getCart(id)
        emit(response)
    }

    fun getCartsCount() = viewModelScope.launch {
        cartRepo.getCartsCount().observeForever {
            if (it != null)
                cartCount.postValue(it.toString())
        }
    }


}