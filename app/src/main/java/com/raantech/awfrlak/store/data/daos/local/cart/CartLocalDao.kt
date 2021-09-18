package com.raantech.awfrlak.store.data.daos.local.cart

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.raantech.awfrlak.store.data.db.ApplicationDB.Companion.TABLE_CART
import com.raantech.awfrlak.store.data.models.home.AccessoriesItem

@Dao
interface CartLocalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCart(accessory: AccessoriesItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCarts(accessories: List<AccessoriesItem>)

    @Query("SELECT * FROM $TABLE_CART ORDER BY id ASC")
    suspend fun getCarts(): List<AccessoriesItem>

    @Query("SELECT * FROM $TABLE_CART WHERE id = :id")
    suspend fun getCarts(id: Int): AccessoriesItem

    @Query("SELECT SUM(count) FROM $TABLE_CART")
    fun getCartsCount(): LiveData<Int>

    @Query("SELECT SUM(count) FROM $TABLE_CART")
    suspend fun getCartsCountInt(): Int?

    @Query("DELETE FROM $TABLE_CART WHERE id = :id")
    suspend fun deleteCart(id: Int)

    @Query("DELETE FROM $TABLE_CART")
    suspend fun clearCart()
}