package com.raantech.awfrlak.store.data.repos.orders

import com.raantech.awfrlak.store.data.api.response.APIResource
import com.raantech.awfrlak.store.data.api.response.ResponseHandler
import com.raantech.awfrlak.store.data.api.response.ResponseWrapper
import com.raantech.awfrlak.store.data.daos.remote.orders.OrdersRemoteDao
import com.raantech.awfrlak.store.data.repos.base.BaseRepo
import com.raantech.awfrlak.store.data.models.orders.OrdersItem
import javax.inject.Inject

class OrdersRepoImp @Inject constructor(
    responseHandler: ResponseHandler,
    private val ordersRemoteDao: OrdersRemoteDao
) : BaseRepo(responseHandler), OrdersRepo {

    override suspend fun getOrders(skip: Int): APIResource<ResponseWrapper<List<OrdersItem>>> {
        return try {
            responseHandle.handleSuccess(ordersRemoteDao.getOrders(skip))
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

    override suspend fun getOrderDetails(orderId: String): APIResource<ResponseWrapper<OrdersItem>> {
        return try {
            responseHandle.handleSuccess(ordersRemoteDao.getOrderDetails(orderId))
        } catch (e: Exception) {
            responseHandle.handleException(e)
        }
    }

}