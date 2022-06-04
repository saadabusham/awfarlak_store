package com.raantech.awfrlak.store.data.models.notification

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Notification(
    @field:SerializedName("created_at_diff_for_humans")
    val createdAtDiffForHumans: String? = null,

    @field:SerializedName("type_id")
    val typeId: Any? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("body")
    val body: String? = null,
    val read: Boolean = false
): Serializable
