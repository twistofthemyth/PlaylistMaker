package com.practicum.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import com.practicum.playlistmaker.search.data.dto.LookupEntityRequest
import com.practicum.playlistmaker.search.data.dto.SearchEntityRequest
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

    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { statusCode = -1 }
        }
        return try {
            when (dto) {
                is SearchEntityRequest -> service.searchEntityByTerm(dto.entity, dto.term)
                    .apply { statusCode = 200 }

                is LookupEntityRequest -> service.lookupEntityById( dto.id)
                    .apply { statusCode = 200 }

                else -> return Response().apply { statusCode = 400 }
            }
        } catch (_: Throwable) {
            Response().apply { statusCode = 500 }
        }
    }
}