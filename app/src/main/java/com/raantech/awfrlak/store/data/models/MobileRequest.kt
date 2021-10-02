package com.raantech.awfrlak.store.data.models

import com.google.gson.annotations.SerializedName

data class MobileRequest(

	@field:SerializedName("is_in_stock")
	val isInStock: Boolean? = null,

	@field:SerializedName("is_active")
	val isActive: Boolean? = null,

	@field:SerializedName("color_id")
	val colorId: Int? = null,

	@field:SerializedName("related_mobiles")
	val relatedMobiles: List<Int?>? = null,

	@field:SerializedName("is_new")
	val isNew: Boolean? = null,

	@field:SerializedName("price")
	val price: Double? = null,

	@field:SerializedName("type_id")
	val typeId: Int? = null,

	@field:SerializedName("storage_id")
	val storageId: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("sim_cards_numbers")
	val simCardsNumbers: Int? = null,

	@field:SerializedName("files")
	val files: Files? = null
)