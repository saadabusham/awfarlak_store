package com.raantech.awfrlak.store.data.models.auth.login2

import com.google.gson.annotations.SerializedName

data class UserDetailsResponseModel(

	@field:SerializedName("is_registered")
	val isRegistered: Boolean? = null,

	@field:SerializedName("user_info")
	var userInfo: UserInfo? = null,

	@field:SerializedName("auth_token")
	val authToken: String? = null
)