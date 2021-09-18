package com.raantech.awfrlak.store.data.models.home

import com.google.gson.annotations.SerializedName

data class Slider(

	@field:SerializedName("slides")
	val slides: List<SlidesItem>? = null,

	@field:SerializedName("dots")
	val dots: Int? = null,

	@field:SerializedName("autoplay_speed")
	val autoplaySpeed: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("arrows")
	val arrows: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("autoplay")
	val autoplay: Int? = null
)