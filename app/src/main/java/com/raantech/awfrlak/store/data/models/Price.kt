package com.raantech.awfrlak.store.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Price(

	@field:SerializedName("amount")
    var amount: String? = null,

	@field:SerializedName("formatted")
	val formatted: String? = null,

	@field:SerializedName("currency")
	val currency: String? = null
):Serializable