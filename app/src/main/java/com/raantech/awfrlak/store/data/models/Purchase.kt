package com.raantech.awfrlak.store.data.models

import com.raantech.awfrlak.store.data.models.home.AccessoriesItem

data class Purchase(
    val id:Int? = 123456,
    val status: String,
    val price:Price? = Price("50.0","50.0 ر.س","ر.س"),
    val date:String? = "2 jan 2021",
    val items: List<AccessoriesItem>
) {
    fun itemsString(): String {
        return items.map { "x${it.count} ${it.name}" }.joinToString(separator = "\n")
    }
}
