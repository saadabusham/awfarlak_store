package com.raantech.awfrlak.store.data.db.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.raantech.awfrlak.store.data.models.home.AccessoryDedicated
import java.lang.reflect.Type

class AccessoryDedicatedConverter {

    @TypeConverter
    fun storedStringToObject(value: String?): AccessoryDedicated? {
        val type: Type = object : TypeToken<AccessoryDedicated?>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun objectToStoredString(data: AccessoryDedicated?): String? {
        val gson = Gson()
        return gson.toJson(data)
    }
}