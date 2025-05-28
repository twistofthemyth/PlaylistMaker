package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.Runnable

interface TrackRepository {
    fun searchTrack(query: String, onFail: Runnable): Pair<Boolean, List<Track>>

    fun addTrackToHistory(track: Track)

    fun clearTrackHistory()

    fun getSearchHistory(): List<Track>

    fun saveSearchInput(value: String)

    fun loadSavedSearchInput() : String
}