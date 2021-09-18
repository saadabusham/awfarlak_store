package com.raantech.awfrlak.store.data.db.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.raantech.awfrlak.store.data.models.home.AccessoryType
import java.lang.reflect.Type

class AccessoryTypeConverter {

    @TypeConverter
    fun storedStringToObject(value: String?): AccessoryType? {
        val type: Type = object : TypeToken<AccessoryType?>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun objectToStoredString(data: AccessoryType?): String? {
        val gson = Gson()
        return gson.toJson(data)
    }
}