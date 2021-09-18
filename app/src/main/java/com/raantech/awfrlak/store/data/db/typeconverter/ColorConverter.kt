package com.raantech.awfrlak.store.data.db.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.raantech.awfrlak.store.data.models.home.Color
import java.lang.reflect.Type

class ColorConverter {

    @TypeConverter
    fun storedStringToObject(value: String?): Color? {
        val type: Type = object : TypeToken<Color?>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun objectToStoredString(data: Color?): String? {
        val gson = Gson()
        return gson.toJson(data)
    }
}