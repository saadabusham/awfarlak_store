package com.raantech.awfrlak.store.data.models

data class Order(
    val id:Int? = 123456,
    val productName: String="iPhon",
    val clientName:String = "saad",
    val status: String,
    val price:Price? = Price("50.0","50.0 ر.س","ر.س"),
    val date:String? = "2 jan 2021",
) {

}
