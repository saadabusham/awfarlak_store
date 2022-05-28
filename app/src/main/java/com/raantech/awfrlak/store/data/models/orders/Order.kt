package com.raantech.awfrlak.store.data.models.orders

import com.google.gson.annotations.SerializedName
import com.raantech.awfrlak.store.data.models.Price

data class Order(

    @field:SerializedName("order_number")
	val orderNumber: String? = null,

    @field:SerializedName("created_at")
	val createdAt: String? = null,

    @field:SerializedName("groups")
	val groups: List<OrdersItem>? = null,

    @field:SerializedName("locale")
	val locale: String? = null,

    @field:SerializedName("payment_method_text")
	val paymentMethodText: String? = null,

    @field:SerializedName("total")
	val total: Price? = null,

    @field:SerializedName("currency_rate")
	val currencyRate: String? = null,

    @field:SerializedName("sub_total")
	val subTotal: Price? = null,

    @field:SerializedName("currency")
	val currency: String? = null,

    @field:SerializedName("vat_percentage")
	val vatPercentage: Int? = null,

    @field:SerializedName("status_text")
	val statusText: String? = null,

    @field:SerializedName("payment_method")
	val paymentMethod: String? = null,

    @field:SerializedName("vat_price")
	val vatPrice: Price? = null,

    @field:SerializedName("status")
	val status: String? = null
)