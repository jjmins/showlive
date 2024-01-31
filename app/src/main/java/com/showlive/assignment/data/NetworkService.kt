package com.showlive.assignment.data

import com.showlive.assignment.data.entity.Characters
import com.showlive.assignment.data.entity.Response
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {
    @GET("v1/public/characters")
    fun getCharacters(
        @Query("nameStartsWith") name: String,
        @Query("apikey") apiKey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: Long,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ) : Single<Response<Characters>>
}