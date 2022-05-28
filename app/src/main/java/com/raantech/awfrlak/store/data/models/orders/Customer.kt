package com.raantech.awfrlak.store.data.models.orders

import com.google.gson.annotations.SerializedName

data class Customer(

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)