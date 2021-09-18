package com.raantech.awfrlak.store.data.db.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.raantech.awfrlak.store.data.models.Price
import com.raantech.awfrlak.store.data.models.home.Store
import java.lang.reflect.Type

class StoreConverter {

    @TypeConverter
    fun storedStringToObject(value: String?): Store? {
        val type: Type = object : TypeToken<Store?>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun objectToStoredString(data: Store?): String? {
        val gson = Gson()
        return gson.toJson(data)
    }
}