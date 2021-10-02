package com.raantech.awfrlak.store.data.models

import com.google.gson.annotations.SerializedName

data class AccessoryRequest(

	@field:SerializedName("is_in_stock")
	val isInStock: Boolean? = null,

	@field:SerializedName("is_active")
	val isActive: Boolean? = null,

	@field:SerializedName("accessory_type_id")
	val accessoryTypeId: Int? = null,

	@field:SerializedName("price")
	val price: Double? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("files")
	val files: Files? = null,

	@field:SerializedName("accessory_dedicated_id")
	val accessoryDedicatedId: Int? = null
)