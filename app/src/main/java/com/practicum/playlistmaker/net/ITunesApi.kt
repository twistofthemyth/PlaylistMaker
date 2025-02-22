package com.practicum.playlistmaker.net

import com.practicum.playlistmaker.model.SearchSongResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {

    @GET("/search?entity=song")
    fun searchSong(@Query("term") searchQuery: String): Call<SearchSongResponse>
}