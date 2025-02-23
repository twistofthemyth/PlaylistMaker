package com.practicum.playlistmaker.net

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ITunesService {

    private val retrofit = Retrofit.Builder()
        .client(OkHttpClient.Builder().build())
        .baseUrl(HOST)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: ITunesApi = retrofit.create(ITunesApi::class.java)

    companion object {
        private val HOST = "https://itunes.apple.com"
    }
}