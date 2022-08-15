package com.raantech.awfrlak.store.data.repos.orders

import com.raantech.awfrlak.store.data.api.response.APIResource
import com.raantech.awfrlak.store.data.api.response.ResponseWrapper
import com.raantech.awfrlak.store.data.models.orders.OrdersItem
import retrofit2.http.Path

interface OrdersRepo {

    suspend fun getOrders(
        skip: Int
    ): APIResource<ResponseWrapper<List<OrdersItem>>>

    suspend fun getOrderDetails(
        orderId: String
    ): APIResource<ResponseWrapper<OrdersItem>>

    suspend fun parcelOrder(
        orderId: String
    ): APIResource<ResponseWrapper<Any>>
}