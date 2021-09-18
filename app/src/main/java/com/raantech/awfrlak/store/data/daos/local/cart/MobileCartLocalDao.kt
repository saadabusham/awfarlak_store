package com.raantech.awfrlak.store.data.daos.local.cart

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.raantech.awfrlak.store.data.db.ApplicationDB.Companion.TABLE_MOBILE_CART
import com.raantech.awfrlak.store.data.models.home.MobilesItem

@Dao
interface MobileCartLocalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCart(mobile: MobilesItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCarts(mobiles: List<MobilesItem>)

    @Query("SELECT * FROM $TABLE_MOBILE_CART ORDER BY id ASC")
    suspend fun getCarts(): List<MobilesItem>

    @Query("SELECT * FROM $TABLE_MOBILE_CART WHERE id = :id")
    suspend fun getCart(id: Int): MobilesItem

    @Query("SELECT SUM(count) FROM $TABLE_MOBILE_CART")
    fun getCartsCount(): LiveData<Int>

    @Query("SELECT SUM(count) FROM $TABLE_MOBILE_CART")
    suspend fun getCartsCountInt(): Int?

    @Query("DELETE FROM $TABLE_MOBILE_CART WHERE id = :id")
    suspend fun deleteCart(id: Int)

    @Query("DELETE FROM $TABLE_MOBILE_CART")
    suspend fun clearCart()
}