package com.raantech.awfrlak.store.data.models.orders

import com.google.gson.annotations.SerializedName
import com.raantech.awfrlak.store.data.enums.OrderStatusEnum
import com.raantech.awfrlak.store.data.models.Price
import com.raantech.awfrlak.store.data.models.home.Store
import java.io.Serializable

data class OrdersItem(

    @field:SerializedName("total")
    val total: Price? = null,

    @field:SerializedName("sub_total")
    val subTotal: Price? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("order_group_number")
    val orderGroupNumber: String? = null,

    @field:SerializedName("vat_percentage")
    val vatPercentage: Int? = null,

    @field:SerializedName("customer")
    val customer: Customer? = null,

    @field:SerializedName("vat_price")
    val vatPrice: Price? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("prodcuts")
    val prodcuts: List<OrderProduct>? = null,

    @field:SerializedName("require_delivery")
    val requireDelivery: Boolean? = null,

    @field:SerializedName("tracking_url")
    val trackingUrl: String? = null,

    @field:SerializedName("waybill_url")
    val waybillUrl: String? = null,

    @field:SerializedName("tracking_number")
    val trackingNumber: String? = null
):Serializable{

    fun showParcel():Boolean{
        return requireDelivery == true && status == OrderStatusEnum.PENDING.value
    }
    fun showTrace():Boolean{
        return requireDelivery == true && status == OrderStatusEnum.PROCESSING.value
    }
}