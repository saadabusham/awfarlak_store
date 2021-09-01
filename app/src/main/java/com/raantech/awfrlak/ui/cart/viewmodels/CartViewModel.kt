package com.raantech.awfrlak.ui.cart.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.raantech.awfrlak.data.models.Price
import com.raantech.awfrlak.data.models.accessories.Accessory
import com.raantech.awfrlak.data.repos.cart.cart.CartRepo
import com.raantech.awfrlak.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepo: CartRepo
) : BaseViewModel() {
    companion object{
    }

    val cartCount:MutableLiveData<String> = MutableLiveData("0")
    val TAX_CONST:Double = 0.15
    var tax:MutableLiveData<Price> = MutableLiveData()
    var subTotal:MutableLiveData<Price> = MutableLiveData()
    var total:MutableLiveData<Price> = MutableLiveData()
    fun updateCartItem(accessory: Accessory) = viewModelScope.launch {
        cartRepo.saveCart(accessory)
    }

    fun deleteCart(id: Int) = viewModelScope.launch {
        cartRepo.deleteCart(id)
    }

    fun getCarts() = liveData {
        val response = cartRepo.loadCarts()
        emit(response)
    }

    fun getCart(id: Int) = liveData {
        val response = cartRepo.getCart(id)
        emit(response)
    }
    fun getCartsCount() = viewModelScope.launch{
        cartRepo.getCartsCount().observeForever {
            if (it != null)
                cartCount.postValue(it.toString())
        }
    }


}