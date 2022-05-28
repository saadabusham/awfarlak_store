package com.raantech.awfrlak.store.ui.main.orders.viewmodels

import androidx.lifecycle.liveData
import com.raantech.awfrlak.store.data.api.response.APIResource
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

    fun getOrders(
        skip: Int
    ) = liveData {
        emit(APIResource.loading())
        val response = ordersRepo.getOrders(skip)
        emit(response)
    }

}