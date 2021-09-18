package com.raantech.awfrlak.store.data.models.wishlist

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WishList(
		@field:SerializedName("id")
		val id: Int? = null,

		@field:SerializedName("entity_type")
		val entity_type: String? = null,

		@field:SerializedName("entity")
		val entity: Any? = null,
) : Serializable