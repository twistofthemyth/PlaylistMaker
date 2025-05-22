package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.dto.SearchSongResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApiService {

    @GET("/search")
    fun searchSong(
        @Query("entity") searchEntity: String,
        @Query("term") searchQuery: String
    ): Call<SearchSongResponse>
}