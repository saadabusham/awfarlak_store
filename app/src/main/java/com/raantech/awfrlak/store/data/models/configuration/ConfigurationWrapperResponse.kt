package com.raantech.awfrlak.store.data.models.configuration

import com.google.gson.annotations.SerializedName

data class ConfigurationWrapperResponse(
    @field:SerializedName("facebook_url")
    val facebookUrl: String? = null,

    @field:SerializedName("app_phone_number")
    val appPhoneNumber: String? = null,

    @field:SerializedName("app_email")
    val appEmail: String? = null,

    @field:SerializedName("instagram_url")
    val instagramUrl: String? = null,

    @field:SerializedName("snapchat_url")
    val snapchatUrl: String? = null,

    @field:SerializedName("linkedin_url")
    val linkedinUrl: String? = null,

    @field:SerializedName("youtube_url")
    val youtubeUrl: String? = null,

    @field:SerializedName("twitter_url")
    val twitterUrl: String? = null
)


