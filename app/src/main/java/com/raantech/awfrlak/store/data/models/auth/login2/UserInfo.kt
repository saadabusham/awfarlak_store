package com.raantech.awfrlak.store.data.models.auth.login2

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserInfo(

	@field:SerializedName("last_login")
	val lastLogin: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("phone_number")
	val phoneNumber: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("store")
	val store: Store? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("address")
	val address: UserAddress? = null
):Serializable