package com.raantech.awfrlak.store.data.models.home

import com.google.gson.annotations.SerializedName

data class HomeResponse(

	@field:SerializedName("slider")
	val slider: Slider? = null,

	@field:SerializedName("mobiles")
	val mobiles: List<MobilesItem>? = null,

	@field:SerializedName("stores")
	val stores: List<Store>? = null,

	@field:SerializedName("accessories")
	val accessories: List<AccessoriesItem>? = null
)