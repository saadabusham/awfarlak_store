package com.raantech.awfrlak.store.ui.orders.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.raantech.awfrlak.store.data.api.response.APIResource
import com.raantech.awfrlak.store.data.models.orders.OrdersItem
import com.raantech.awfrlak.store.data.repos.configuration.ConfigurationRepo
import com.raantech.awfrlak.store.data.repos.orders.OrdersRepo
import com.raantech.awfrlak.store.ui.base.viewmodel.BaseViewModel
import com.raantech.awfrlak.store.utils.pref.SharedPreferencesUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val configurationRepo: ConfigurationRepo,
    private val sharedPreferencesUtil: SharedPreferencesUtil,
    private val ordersRepo: OrdersRepo
) : BaseViewModel() {

    var orderIdToView : String? = null
    var orderItemToView : MutableLiveData<OrdersItem> = MutableLiveData()
    fun getOrderDetails(
        orderId: String
    ) = liveData {
        emit(APIResource.loading())
        val response = ordersRepo.getOrderDetails(orderId)
        emit(response)
    }

    fun parcelOrder(
        orderId: String
    ) = liveData {
        emit(APIResource.loading())
        val response = ordersRepo.parcelOrder(orderId)
        emit(response)
    }

}