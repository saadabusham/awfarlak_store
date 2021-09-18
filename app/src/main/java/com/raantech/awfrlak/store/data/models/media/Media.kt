package com.raantech.awfrlak.store.data.models.media

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Media(

	@field:SerializedName("extension")
	val extension: String? = null,

	@field:SerializedName("filename")
	val filename: String? = null,

	@field:SerializedName("size")
	val size: String? = null,

	@field:SerializedName("mime")
	val mime: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("url")
	val url: String? = null
):Serializable