package com.raantech.awfrlak.store.data.models

import com.google.gson.annotations.SerializedName

data class Files(

	@field:SerializedName("additional_images")
	val additionalImages: List<Int>? = null,

	@field:SerializedName("base_image")
	val baseImage: Int? = null
)