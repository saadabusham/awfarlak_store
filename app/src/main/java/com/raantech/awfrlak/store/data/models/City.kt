package com.raantech.awfrlak.store.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class City(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("id")
	val id: Int,
	var selected: Boolean = false
):Serializable