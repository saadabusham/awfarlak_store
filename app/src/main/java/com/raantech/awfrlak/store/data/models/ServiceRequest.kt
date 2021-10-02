package com.raantech.awfrlak.store.data.models

import com.google.gson.annotations.SerializedName

data class ServiceRequest(

	@field:SerializedName("service_completion_time")
	val serviceCompletionTime: String? = null,

	@field:SerializedName("has_delivery")
	val hasDelivery: Boolean? = null,

	@field:SerializedName("is_active")
	val isActive: Boolean? = null,

	@field:SerializedName("price")
	val price: Double? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("files")
	val files: Files? = null,

	@field:SerializedName("is_available")
	val isAvailable: Boolean? = null
)