package com.raantech.awfrlak.store.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.raantech.awfrlak.store.data.daos.local.cart.CartLocalDao
import com.raantech.awfrlak.store.data.daos.local.cart.MobileCartLocalDao
import com.raantech.awfrlak.store.data.db.typeconverter.*
import com.raantech.awfrlak.store.data.models.home.AccessoriesItem
import com.raantech.awfrlak.store.data.models.home.MobilesItem

@Database(
        entities = [
            AccessoriesItem::class,
            MobilesItem::class
        ], version = 1
)

@TypeConverters(
        CategoryConverter::class,
        MediaConverter::class,
        MediaListConverter::class,
        PriceConverter::class,
        ColorConverter::class,
        MobileListConverter::class,
        StoreConverter::class,
        StorageConverter::class,
        TypeConverter::class,
        AccessoryTypeConverter::class,
        AccessoryDedicatedConverter::class
)

abstract class ApplicationDB : RoomDatabase() {

    abstract fun cartLocalDao(): CartLocalDao
    abstract fun mobileCartLocalDao(): MobileCartLocalDao

    companion object {
        const val DATABASE_NAME = "awfrlak.db"
        const val TABLE_CART = "cartTable"
        const val TABLE_MOBILE_CART = "mobileCartTable"
    }

}