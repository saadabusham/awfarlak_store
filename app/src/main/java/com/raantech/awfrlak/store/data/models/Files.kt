package com.raantech.awfrlak.store.data.models

import com.google.gson.annotations.SerializedName
import com.raantech.awfrlak.store.data.models.media.Media

data class Files(

		@field:SerializedName("base_image")
		val baseImage: Int? = null,

		@field:SerializedName("additional_images")
		val additionalImages: List<Int>? = null,
)