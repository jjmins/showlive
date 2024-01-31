package com.showlive.assignment.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.showlive.assignment.data.entity.Thumbnail

class Converters {
    @TypeConverter
    fun fromThumbnail(thumbnail: Thumbnail): String {
        return Gson().toJson(thumbnail)
    }

    @TypeConverter
    fun toThumbnail(json: String): Thumbnail {
        val type = object : TypeToken<Thumbnail>() {}.type
        return Gson().fromJson(json, type)
    }
}