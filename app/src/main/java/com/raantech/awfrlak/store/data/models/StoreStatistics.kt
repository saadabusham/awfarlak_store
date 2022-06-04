package com.raantech.awfrlak.store.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class StoreStatistics(

    @field:SerializedName("service_requests")
    val serviceRequests: Int? = 0,

    @field:SerializedName("total_mobiles")
    val totalMobiles: Int? = 0,

    @field:SerializedName("total_service")
    val totalService: Int? = 0,

    @field:SerializedName("total_accessory")
    val totalAccessory: Int? = 0,

    @field:SerializedName("number_of_sales")
    val numberOfSales: Int? = 0,

    @field:SerializedName("total_sales")
    val totalSales: Price? = Price(formatted = "$0")
) : Serializable {
    fun isTheDataEmpty() =
        (totalMobiles ?: 0) == 0 && (totalAccessory ?: 0) == 0 && (totalService ?: 0) == 0
}