package com.showlive.assignment.data.repository

import com.showlive.assignment.data.entity.Characters
import io.reactivex.Single

interface MarvelRepository {
    fun getCharacters(name: String, offset: Int = 0, limit: Int = 10): Single<Characters>
}