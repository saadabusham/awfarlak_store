package com.raantech.awfrlak.store.data.models.home

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Color(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
): Serializable