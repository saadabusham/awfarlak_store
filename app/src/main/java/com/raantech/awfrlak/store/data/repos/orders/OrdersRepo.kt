package com.raantech.awfrlak.store.data.repos.orders

import com.raantech.awfrlak.store.data.api.response.APIResource
import com.raantech.awfrlak.store.data.api.response.ResponseWrapper
import com.raantech.awfrlak.store.data.models.orders.Order
import com.raantech.awfrlak.store.data.models.orders.OrderDetails

interface OrdersRepo {

    suspend fun getOrders(
        skip: Int
    ): APIResource<ResponseWrapper<List<Order>>>

    suspend fun getOrderDetails(
        orderId: Int
    ): APIResource<ResponseWrapper<OrderDetails>>

}