package com.raantech.awfrlak.store.data.db.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.raantech.awfrlak.store.data.models.Price
import java.lang.reflect.Type

class PriceConverter {

    @TypeConverter
    fun storedStringToObject(value: String?): Price? {
        val type: Type = object : TypeToken<Price?>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun objectToStoredString(data: Price?): String? {
        val gson = Gson()
        return gson.toJson(data)
    }
}