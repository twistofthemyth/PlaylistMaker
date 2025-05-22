package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.Runnable
import java.util.function.Consumer

interface TrackInteractor {
    fun searchTracks(query: String, onSuccess: Consumer<List<Track>>, onFail: Runnable)

    fun addTrackToHistory(track: Track)

    fun clearHistory()

    fun getSearchHistory(consumer: Consumer<List<Track>>)

    fun saveSearchQuery(value: String)

    fun getSavedSearchQuery(): String
}