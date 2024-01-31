package com.showlive.assignment.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.showlive.assignment.data.entity.Character
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM character ORDER BY timestamp ASC;")
    fun getAll() : Flowable<Array<Character>>

    @Query("SELECT COUNT(*) FROM character WHERE id = :id")
    fun canNotSaveCharacter(id: Int) : Single<Boolean>

    @Query("SELECT COUNT(*) >= 5 AS is_items_greater_than_5 FROM character")
    fun areCharacterThan5(): Single<Boolean>

    @Query("DELETE FROM character WHERE timestamp = (SELECT MIN(timestamp) FROM character)")
    fun deleteOldItem(): Completable

    @Insert
    fun insert(character: Character) : Completable

    @Delete
    fun delete(character: Character) : Completable
}