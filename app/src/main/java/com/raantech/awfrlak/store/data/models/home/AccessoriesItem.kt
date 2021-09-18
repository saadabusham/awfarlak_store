package com.raantech.awfrlak.store.data.models.home

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.raantech.awfrlak.store.data.db.ApplicationDB
import com.raantech.awfrlak.store.data.models.Price
import com.raantech.awfrlak.store.data.models.media.Media
import java.io.Serializable

@Entity(tableName = ApplicationDB.TABLE_CART)
data class AccessoriesItem(

		@field:SerializedName("additional_images")
		@ColumnInfo(name = "additional_images")
		val additionalImages: List<Media>? = null,

		@field:SerializedName("is_in_stock")
		@ColumnInfo(name = "is_in_stock")
		val isInStock: Boolean? = null,

		@field:SerializedName("base_image")
		@ColumnInfo(name = "base_image")
		val baseImage: Media? = null,

		@field:SerializedName("is_active")
		@ColumnInfo(name = "is_active")
		val isActive: Boolean? = null,

		@field:SerializedName("accessory_type")
		@ColumnInfo(name = "accessory_type")
		val accessoryType: AccessoryType? = null,

		@field:SerializedName("price")
		@ColumnInfo(name = "price")
		val price: Price? = null,

		@field:SerializedName("name")
		@ColumnInfo(name = "name")
		val name: String? = null,

		@field:SerializedName("description")
		@ColumnInfo(name = "description")
		val description: String? = null,

		@field:SerializedName("id")
		@PrimaryKey(autoGenerate = false)
		@ColumnInfo(name = "id")
		val id: Int? = null,

		@field:SerializedName("store")
		@ColumnInfo(name = "store")
		val store: Store? = null,

		@field:SerializedName("is_wishlist")
		@ColumnInfo(name = "is_wishlist")
		var isWishlist: Boolean? = null,

		@field:SerializedName("accessory_dedicated")
		@ColumnInfo(name = "accessory_dedicated")
		val accessoryDedicated: AccessoryDedicated? = null,

		@field:SerializedName("count")
		@ColumnInfo(name = "count")
		var count: Int? = 1
) : Serializable