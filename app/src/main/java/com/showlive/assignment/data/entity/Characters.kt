package com.showlive.assignment.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

data class Characters(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val results: List<Character>
)


@Entity
data class Character(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val thumbnail: Thumbnail,
    @Expose(serialize = false, deserialize = false)
    val isFavorite: Boolean,
    val timestamp: Long,
) : CharacterItem {
    val thumbnailUrl: String
        get() = "${thumbnail.path}.${thumbnail.extension}"
}


data class Thumbnail(
    val path: String,
    val extension: String
)