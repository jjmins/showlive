package com.showlive.assignment.data.repository

import android.util.Log
import com.showlive.assignment.data.NetworkService
import com.showlive.assignment.data.entity.Characters
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.nio.charset.StandardCharsets.UTF_8
import java.security.MessageDigest
import javax.inject.Inject

class MarvelRepositoryImpl @Inject constructor(
    private val api: NetworkService
): MarvelRepository {
    private val apiKey: String
        get() = "c795ec9969b5d59bfad26abffd09b269"

    private val privateKey: String
        get() = "184198fba69871f6a2344374ab8a7436a7167832"

    private fun generateMD5Hash(time: Long, private: String, public: String): String {
        val input = "$time$private$public"
        return MessageDigest.getInstance("MD5").digest(input.toByteArray(UTF_8)).joinToString("") { "%02x".format(it) }
    }

    override fun getCharacters(name: String, offset: Int, limit: Int): Single<Characters> {
        val time = System.currentTimeMillis()
        return api.getCharacters(
            name = name,
            apiKey = apiKey,
            hash = generateMD5Hash(time, privateKey, apiKey),
            ts = time,
            offset = offset,
            limit = limit
        )
            .map { it.data }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}