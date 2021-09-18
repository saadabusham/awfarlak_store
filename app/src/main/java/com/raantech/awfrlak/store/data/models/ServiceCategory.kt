package com.raantech.awfrlak.store.data.models

import com.google.gson.annotations.SerializedName
import com.raantech.awfrlak.store.data.models.media.Media
import java.io.Serializable

data class ServiceCategory(

	@field:SerializedName("has_sub")
	val hasSub: Boolean? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("logo")
	val logo: Media? = null,

	@field:SerializedName("banner")
	val banner: String? = null,

	@field:SerializedName("id")
	val id: Int,
	var selected: Boolean = false
):Serializable