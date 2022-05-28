package com.raantech.awfrlak.store.data.daos.remote.orders


import com.raantech.awfrlak.store.data.api.response.ResponseWrapper
import com.raantech.awfrlak.store.data.common.NetworkConstants
import com.raantech.awfrlak.store.data.models.orders.Order
import com.raantech.awfrlak.store.data.models.orders.OrderDetails
import retrofit2.http.*

interface OrdersRemoteDao {

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("orders")
    suspend fun getOrders(
        @Query("skip") skip: Int
    ): ResponseWrapper<List<Order>>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("orders/show/{orderId}")
    suspend fun getOrderDetails(
        @Path("orderId") orderId: Int
    ): ResponseWrapper<OrderDetails>

}