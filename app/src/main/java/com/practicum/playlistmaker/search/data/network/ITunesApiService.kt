package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.dto.SearchSongResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApiService {

    @GET("/search")
    suspend fun searchSong(
        @Query("entity") searchEntity: String,
        @Query("term") searchQuery: String
    ): SearchSongResponse
}