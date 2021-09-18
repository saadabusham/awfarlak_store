package com.raantech.awfrlak.store.data.repos.cart.mobilecart

import androidx.lifecycle.LiveData
import com.raantech.awfrlak.store.data.models.home.MobilesItem

interface MobileCartRepo {

    suspend fun saveCart(mobile: MobilesItem)
    suspend fun saveCarts(mobiles: List<MobilesItem>)
    suspend fun loadCarts(): List<MobilesItem>
    suspend fun getCart(id: Int): MobilesItem
    fun getCartsCount(): LiveData<Int>
    suspend fun getCartsCountInt(): Int?
    suspend fun deleteCart(id:Int)
    suspend fun clearCart()
}