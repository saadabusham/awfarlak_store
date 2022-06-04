package com.raantech.awfrlak.store.data.models.orders

import com.google.gson.annotations.SerializedName
import com.raantech.awfrlak.store.data.models.Price
import java.io.Serializable

data class OrderProduct(

	@field:SerializedName("entity_type")
	val entityType: String? = null,

	@field:SerializedName("qty")
	val qty: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("unit_price")
	val unitPrice: Price? = null,

	@field:SerializedName("line_total")
	val lineTotal: Price? = null,

	@field:SerializedName("entity")
	val entity: Entity? = null
): Serializable