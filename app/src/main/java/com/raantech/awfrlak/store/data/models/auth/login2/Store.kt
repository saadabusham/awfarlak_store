package com.raantech.awfrlak.store.data.models.auth.login2

import com.google.gson.annotations.SerializedName
import com.raantech.awfrlak.store.data.models.media.Media

data class Store(

	@field:SerializedName("cover")
	val cover: String? = null,

	@field:SerializedName("additional_images")
	val additionalImages: List<Media>? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("responsible_person")
	val responsiblePerson: String? = null,

	@field:SerializedName("latitude")
	val latitude: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("logo")
	val logo: Media? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("is_wishlist")
	val isWishlist: Boolean? = null,

	@field:SerializedName("longitude")
	val longitude: String? = null
)