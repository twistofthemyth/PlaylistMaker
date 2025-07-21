package com.practicum.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import com.practicum.playlistmaker.search.data.dto.SearchSongRequest
import com.practicum.playlistmaker.util.data_utils.AbstractNetworkClient
import com.practicum.playlistmaker.util.data_utils.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { statusCode = -1 }
        }
        if (dto !is SearchSongRequest) {
            return Response().apply { statusCode = 400 }
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = service.searchSong(dto.entity, dto.term)
                response.apply { statusCode = 200 }
            } catch (_: Throwable) {
                Response().apply { statusCode = 500 }
            }
        }
    }
}