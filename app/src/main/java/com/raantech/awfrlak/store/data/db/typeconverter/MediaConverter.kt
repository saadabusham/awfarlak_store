package com.raantech.awfrlak.store.data.db.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.raantech.awfrlak.store.data.models.media.Media
import java.lang.reflect.Type

class MediaConverter {

    @TypeConverter
    fun storedStringToObject(value: String?): Media? {
        val type: Type = object : TypeToken<Media?>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun objectToStoredString(data: Media?): String? {
        val gson = Gson()
        return gson.toJson(data)
    }
}