package com.raantech.awfrlak.store.data.models.orders

import com.google.gson.annotations.SerializedName
import com.raantech.awfrlak.store.data.models.media.Media
import java.io.Serializable

data class Entity(

	@field:SerializedName("base_image")
	val baseImage: Media? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null
): Serializable