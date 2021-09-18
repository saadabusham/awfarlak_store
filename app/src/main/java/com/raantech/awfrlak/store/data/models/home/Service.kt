package com.raantech.awfrlak.store.data.models.home

import com.google.gson.annotations.SerializedName
import com.raantech.awfrlak.store.data.models.Price
import com.raantech.awfrlak.store.data.models.media.Media
import java.io.Serializable

data class Service(

	@field:SerializedName("service_completion_time")
	val serviceCompletionTime: String? = null,

	@field:SerializedName("has_delivery")
	val hasDelivery: Boolean? = null,

	@field:SerializedName("is_active")
	val isActive: Boolean? = null,

	@field:SerializedName("price")
	val price: Price? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("store")
	val store: Store? = null,

	@field:SerializedName("is_wishlist")
	var isWishlist: Boolean? = null,

	@field:SerializedName("is_available")
	val isAvailable: Boolean? = null,

	@field:SerializedName("cover")
	val cover: Media? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("responsible_person")
	val responsiblePerson: String? = null,

	@field:SerializedName("latitude")
	val latitude: String? = null,

	@field:SerializedName("logo")
	val logo: Media? = null,

	@field:SerializedName("longitude")
	val longitude: String? = null
):Serializable