package com.practicum.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import com.practicum.playlistmaker.search.data.dto.SearchSongRequest
import com.practicum.playlistmaker.util.data_utils.AbstractNetworkClient
import com.practicum.playlistmaker.util.data_utils.Response
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ITunesClient(context: Context) : AbstractNetworkClient(
    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
) {

    private val url = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .client(OkHttpClient.Builder().build())
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service: ITunesApiService = retrofit.create(ITunesApiService::class.java)

    override fun doRequest(dto: Any): Response {
        if (isConnected() == false) {
            return Response().apply { statusCode = -1 }
        }
        if (dto !is SearchSongRequest) {
            return Response().apply { statusCode = 400 }
        }

        val response = service.searchSong(dto.entity, dto.term).execute()
        val body = response.body()
        return if (body != null) {
            body.apply { statusCode = response.code() }
        } else {
            Response().apply { statusCode = response.code() }
        }
    }
}