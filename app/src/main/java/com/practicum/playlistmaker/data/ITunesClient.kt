package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.dto.Response
import com.practicum.playlistmaker.data.dto.SearchSongRequest
import com.practicum.playlistmaker.data.network.ITunesApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class ITunesClient : NetworkClient {

    private val url = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .client(OkHttpClient.Builder().build())
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service: ITunesApiService = retrofit.create(ITunesApiService::class.java)

    override fun doRequest(dto: Any): Response {
        try {
            if (dto is SearchSongRequest) {
                val response = service.searchSong(dto.entity, dto.term).execute();
                val body = response.body() ?: Response()
                return body.apply { statusCode = response.code() }
            } else {
                return Response().apply { statusCode = 400 }
            }
        } catch (e: IOException) {
            return Response().apply { statusCode = 400 }
        }
    }
}