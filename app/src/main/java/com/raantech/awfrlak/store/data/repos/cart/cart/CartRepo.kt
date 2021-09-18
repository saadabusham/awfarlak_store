package com.raantech.awfrlak.store.data.repos.cart.cart

import androidx.lifecycle.LiveData
import com.raantech.awfrlak.store.data.models.home.AccessoriesItem

interface CartRepo {

    suspend fun saveCart(accessory: AccessoriesItem)
    suspend fun saveCarts(accessories: List<AccessoriesItem>)
    suspend fun loadCarts(): List<AccessoriesItem>
    suspend fun getCart(id: Int): AccessoriesItem
    fun getCartsCount(): LiveData<Int>
    suspend fun getCartsCountInt(): Int?
    suspend fun deleteCart(id:Int)
    suspend fun clearCart()
}