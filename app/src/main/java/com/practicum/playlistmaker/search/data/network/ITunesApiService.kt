package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.dto.SearchEntityResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApiService {

    @GET("/search")
    suspend fun searchEntityByTerm(
        @Query("entity") searchEntity: String,
        @Query("term") searchQuery: String
    ): SearchEntityResponse


    @GET("/lookup")
    suspend fun lookupEntityById(@Query("id") entityId: String): SearchEntityResponse
}