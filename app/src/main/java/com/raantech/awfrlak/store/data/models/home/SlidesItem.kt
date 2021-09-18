package com.raantech.awfrlak.store.data.models.home

import com.google.gson.annotations.SerializedName

data class SlidesItem(

	@field:SerializedName("call_to_action_url")
	val callToActionUrl: String? = null,

	@field:SerializedName("caption_1")
	val caption1: String? = null,

	@field:SerializedName("call_to_action_text")
	val callToActionText: String? = null,

	@field:SerializedName("banner")
	val banner: String? = null,

	@field:SerializedName("caption_2")
	val caption2: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("direction")
	val direction: String? = null,

	@field:SerializedName("open_in_new_window")
	val openInNewWindow: Boolean? = null
)