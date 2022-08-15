package com.raantech.awfrlak.store.data.models.auth.login2

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserAddress(

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("street")
	val street: String? = null,

	@field:SerializedName("district")
	val district: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("building_number")
	val buildingNumber: String? = null,

	@field:SerializedName("latitude")
	val latitude: Double? = null,

	@field:SerializedName("longitude")
	val longitude: Double? = null
):Serializable