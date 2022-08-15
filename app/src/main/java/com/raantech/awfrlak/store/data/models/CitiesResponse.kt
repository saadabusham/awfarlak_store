package com.raantech.awfrlak.store.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CitiesResponse(

	@field:SerializedName("cities")
	val cities: List<City>? = null
):Serializable