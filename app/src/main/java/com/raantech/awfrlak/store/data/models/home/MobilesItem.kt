package com.raantech.awfrlak.store.data.models.home

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.raantech.awfrlak.store.data.db.ApplicationDB
import com.raantech.awfrlak.store.data.models.Price
import com.raantech.awfrlak.store.data.models.media.Media
import java.io.Serializable

@Entity(tableName = ApplicationDB.TABLE_MOBILE_CART)
data class MobilesItem(

		@field:SerializedName("additional_images")
		@ColumnInfo(name = "additional_images")
		val additionalImages: List<Media>? = null,

		@field:SerializedName("is_active")
		@ColumnInfo(name = "is_active")
		val isActive: Boolean? = null,

		@field:SerializedName("color")
		@ColumnInfo(name = "color")
		val color: Color? = null,

		@field:SerializedName("related_mobiles")
		@ColumnInfo(name = "related_mobiles")
		val relatedMobiles: List<MobilesItem>? = null,

		@field:SerializedName("is_new")
		@ColumnInfo(name = "is_new")
		val isNew: Boolean? = null,

		@field:SerializedName("sim_cards_numbers")
		@ColumnInfo(name = "sim_cards_numbers")
		val simCardsNumbers: String? = null,

		@field:SerializedName("description")
		@ColumnInfo(name = "description")
		val description: String? = null,

		@field:SerializedName("store")
		@ColumnInfo(name = "store")
		val store: Store? = null,

		@field:SerializedName("storage")
		@ColumnInfo(name = "storage")
		val storage: Storage? = null,

		@field:SerializedName("type")
		@ColumnInfo(name = "type")
		val type: Type? = null,

		@field:SerializedName("is_wishlist")
		@ColumnInfo(name = "is_wishlist")
        var isWishlist: Boolean? = null,

		@field:SerializedName("is_in_stock")
		@ColumnInfo(name = "is_in_stock")
		val isInStock: Boolean? = null,

		@field:SerializedName("base_image")
		@ColumnInfo(name = "base_image")
		val baseImage: Media? = null,

		@field:SerializedName("price")
		@ColumnInfo(name = "price")
		val price: Price? = null,

		@field:SerializedName("name")
		@ColumnInfo(name = "name")
		val name: String? = null,

		@PrimaryKey(autoGenerate = false)
		@field:SerializedName("id")
		@ColumnInfo(name = "id")
		val id: Int? = null,

		@field:SerializedName("count")
		@ColumnInfo(name = "count")
		var count: Int? = 1
) : Serializable {
    fun simCardNumberInt(): Int {
        return simCardsNumbers?.toIntOrNull() ?: 0
    }
}