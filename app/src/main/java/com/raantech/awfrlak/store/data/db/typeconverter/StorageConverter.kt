package com.raantech.awfrlak.store.data.db.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.raantech.awfrlak.store.data.models.Price
import com.raantech.awfrlak.store.data.models.home.Storage
import java.lang.reflect.Type

class StorageConverter {

    @TypeConverter
    fun storedStringToObject(value: String?): Storage? {
        val type: Type = object : TypeToken<Storage?>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun objectToStoredString(data: Storage?): String? {
        val gson = Gson()
        return gson.toJson(data)
    }
}