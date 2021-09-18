package com.raantech.awfrlak.store.data.db.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.raantech.awfrlak.store.data.models.home.MobilesItem
import java.lang.reflect.Type

class MobileListConverter {

    @TypeConverter
    fun storedStringToObject(value: String?): List<MobilesItem>? {
        val listType: Type = object : TypeToken<List<MobilesItem>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun objectToStoredString(list: List<MobilesItem>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}